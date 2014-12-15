// www.theguardian.com/news/datablog/2012/jul/22/gun-homicides-ownership-world-list
// https://docs.google.com/spreadsheets/d/1xgTEAQbTGupCpBwctgQW5-Hveru33sz0a1Z5zZXDhxg/edit?usp=sharing
// https://docs.google.com/spreadsheets/d/1BdrLCyR6kNcIM4yJDxIbullmQl6c5snXR8hhVcaR9rs/edit?usp=sharing
// add other countries from wikipedia
var categories = [
// TODO: title and displaytitle
{"id": "homicides", "title": "Homicides per 100,000", "max": 90.4},
{"id": "firearms-per-100", "title": "Average firearms per 100 people", "max": 88.8},
{"id": "percent-homicides", "title": "% of homicides by firearm", "max": 100},
{"id": "firearm-homicides", "title": "Homicide by firearm rate per 100,000", "max": 68.43}];

var initial_cat = 0;
var dispatch;


function start() {
    dispatch = d3.dispatch("load", "statechange");

    d3.csv("data/morbid.csv", function(error, countries) {
        if (error) throw error;

        dispatch.load(countries);
        dispatch.statechange(categories[initial_cat]);
    });

    dispatch.on("load.map", function(countries) {
        // key value store such that ALB: "Albania", ALB, 100 etc.
        var countriesById = d3.map();

        countries.forEach(function(d) {
            d.fillKey = d["Code"];
            countriesById.set(d["Code"], d);
        });
       

        dispatch.on("statechange.map", function(category) {
            $("#map").empty();

            countriesById.forEach(function(c) {
                var d = countriesById.get(c);
                if (d[category.title]) {
                    // TODO: convert to hsl instead, so we can avoid dullness
                    d.fill = "rgba(138,7,7," + String((parseFloat(d[category.title]) / category.max) + 0.2) + ")";
                } else {
                    d.fill = "rgba(100,100,100, 0.3)";
                    // TODO: http://stackoverflow.com/questions/13069446/simple-fill-pattern-in-svg-diagonal-hatching
                }
            });

            var fills = {};
            fills["defaultFill"] = "rgba(100,100,100, 0.3)";
            countriesById.forEach(function(d) {
                fills[d] = countriesById.get(d).fill;
            });
            $("#map").datamaps({
                fills: fills,
                data: countriesById._,
                geographyConfig: {
                    popupTemplate: function(geo, country_data) {
                        var inner = "<div class='country-name'>" + geo.properties.name + "</div>";
                        if (country_data && country_data[category.title]) {
                            inner += category.title + ': ' + country_data[category.title];
                            if (country_data["Rank by rate of ownership"]) {
                                inner += ("<br></br>World Rank by Rate of Ownership: " + country_data["Rank by rate of ownership"]);
                            }
                            if (country_data["Average total all civilian firearms"]) {
                                inner += ("<br>Average total all civilian firearms: " + country_data["Average total all civilian firearms"]);
                            }
                            if (country_data["Number of homicides by firearm"]) {
                                inner += ("<br>Number of homicides by firearm: " + country_data["Number of homicides by firearm"]);
                            }
                        } else {
                            inner += "No data available";
                        }
                        return '<div class="hoverinfo">' + inner + '</div>';
                    },
                    highlightOnHover: false,
                    borderColor: "rgba(255,255,255, 0.2)"
                }
            });
        });
    });

    dispatch.on("load.bubblechart", function(countries) {
        var x = 'Average firearms per 100 people';

        var active_countries = countries.filter(function(d) {return d[x];} );
        var x_cols = [x].concat(active_countries.map(function(c) {return parseFloat(c[x]);}));

        var chart = c3.generate({
            data: {
                x: x,
                columns:[
                    x_cols
                ],
                type: 'scatter'
            },
            tooltip: {
                format: {
                     title: function (x) {return "";},
                     name: function(name, ratio, id, index) {return "<b>" + active_countries[index]["Country"]+"</b>" + "<br>" + name;}
                }
            },
            point: {
                r: function(d) {
                    return Math.sqrt(active_countries[d.index]["Population"]) / 1000;
                }
            },
            axis: {
                x: {
                    label: x,
                    tick: {
                        fit: false
                    },
                    max: 100
                }
            }
        });
        dispatch.on("statechange.bubblechart", function(category) {
            chart.unload({
                ids: categories.map(function(c) {return c.title != x ? c.title : null;})
            });
            var y = category.title;
            chart.load({
                x: x,
                columns: [
                    x_cols,
                    [y].concat(active_countries.map(function(c) {return parseFloat(c[y]);}))
                ],
                axis: {
                    y: {
                        label: y,
                        min: 0,
                        max: 100
                    }
                }
            });
        });
    });
}

$(document).ready(function() {
    var labels = "";
    categories.forEach(function(c) {
        if (c.id == categories[initial_cat].id) {
            labels += '<input type="radio" id="' + c.id + '" checked="checked" name="radio"><label for="' + c.id + '">' + c.title + '</label></input>';
        } else {
            labels += '<input type="radio" id="' + c.id + '" name="radio"><label for="' + c.id + '">' + c.title + '</label></input>';
        }
    });
    $("#display-choice").html(labels);
    $("#display-choice").buttonset();

    start();
    maps = $(".map");
    $(window).resize(function() {
        start();
        //TODO: keep tab selected open on resize
    });
    $("#display-choice").click(function() {
        var id = $("input:radio[name ='radio']:checked").prop("id");
        var category;
        categories.forEach(function (c) {
            if (id == c.id) {
                category = c;
            }
        });
        if (category) {
            dispatch.statechange(category);
        }
    });
});



