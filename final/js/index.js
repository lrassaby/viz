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
        var x = 'Average Firearms per 100 People';
        var chart = c3.generate({
            data: {
                columns:[
                    ["x"].concat(countries.map(function(c) {return c[x];}))
                ],
                type: 'scatter'
            },

            axis: {
                x: {
                    label: 'Average Firearms per 100 People',
                    tick: {
                        fit: false
                    }
                },
                y: {
                }
            }
        });
        dispatch.on("statechange.bubblechart", function(category) {

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


function makeBC() {

    // setTimeout(function () {
    //     chart.load({
    //         xs: {
    //             virginica: 'virginica_x'
    //         },
    //         columns: [
    //             ["virginica_x", 3.3, 2.7, 3.0, 2.9, 3.0, 3.0, 2.5, 2.9, 2.5, 3.6, 3.2, 2.7, 3.0, 2.5, 2.8, 3.2, 3.0, 3.8, 2.6, 2.2, 3.2, 2.8, 2.8, 2.7, 3.3, 3.2, 2.8, 3.0, 2.8, 3.0, 2.8, 3.8, 2.8, 2.8, 2.6, 3.0, 3.4, 3.1, 3.0, 3.1, 3.1, 3.1, 2.7, 3.2, 3.3, 3.0, 2.5, 3.0, 3.4, 3.0],
    //             ["virginica", 2.5, 1.9, 2.1, 1.8, 2.2, 2.1, 1.7, 1.8, 1.8, 2.5, 2.0, 1.9, 2.1, 2.0, 2.4, 2.3, 1.8, 2.2, 2.3, 1.5, 2.3, 2.0, 2.0, 1.8, 2.1, 1.8, 1.8, 1.8, 2.1, 1.6, 1.9, 2.0, 2.2, 1.5, 1.4, 2.3, 2.4, 1.8, 1.8, 2.1, 2.4, 2.3, 1.9, 2.3, 2.5, 2.3, 1.9, 2.0, 2.3, 1.8],
    //         ]
    //     });
    // }, 1000);


    // setTimeout(function () {
    //     chart.load({
    //         columns: [
    //             ["virginica", 0.2, 0.2, 0.2, 0.2, 0.2, 0.4, 0.3, 0.2, 0.2, 0.1, 0.2, 0.2, 0.1, 0.1, 0.2, 0.4, 0.4, 0.3, 0.3, 0.3, 0.2, 0.4, 0.2, 0.5, 0.2, 0.2, 0.4, 0.2, 0.2, 0.2, 0.2, 0.4, 0.1, 0.2, 0.2, 0.2, 0.2, 0.1, 0.2, 0.2, 0.3, 0.3, 0.2, 0.6, 0.4, 0.3, 0.2, 0.2, 0.2, 0.2],
    //         ]
    //     });
    // }, 3000);
}

//Pie chart: http://bl.ocks.org/mbostock/3887235
//Bubble chart: http://dimplejs.org/examples_viewer.html?id=bubbles_vertical_lollipop
