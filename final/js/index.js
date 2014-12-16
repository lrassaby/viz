// www.theguardian.com/news/datablog/2012/jul/22/gun-homicides-ownership-world-list
// https://docs.google.com/spreadsheets/d/1xgTEAQbTGupCpBwctgQW5-Hveru33sz0a1Z5zZXDhxg/edit?usp=sharing
// https://docs.google.com/spreadsheets/d/1BdrLCyR6kNcIM4yJDxIbullmQl6c5snXR8hhVcaR9rs/edit?usp=sharing
// add other countries from wikipedia
var categories = [
{"id": "homicides", "title": "Homicides per 100,000", "display_title": "Number of homicides per 100,000 People per Year", "max": 90.4},
{"id": "firearms-per-100", "title": "Average firearms per 100 people", "display_title": "Average Number of Firearms per 100 People", "max": 88.8},
{"id": "percent-homicides", "title": "% of homicides by firearm", "display_title": "Percent of Homicides Committed by Firearm", "max": 100},
{"id": "firearm-homicides", "title": "Homicide by firearm rate per 100,000", "display_title": "Homicide by firearm rate per 100,000 People per Year", "max": 68.43}];

var initial_cat = 0;
var dispatch;


function start() {
    var getCountryData = function(countries, country_name) {
        for (var i in countries) {
            if (countries[i]["Country"] == country_name) {
                return countries[i];
            }
        }
    };

    // var spectrum = d3.interpolateRgb(d3.rgb(175, 148, 151), d3.rgb(100, 7, 7));
    var spectrum = d3.interpolateRgb(d3.rgb(200, 200, 180), d3.rgb(100, 7, 7));
    dispatch = d3.dispatch("load", "statechange");

    d3.csv("data/morbid.csv", function(error, countries) {
        if (error) throw error;

        dispatch.load(countries);
        dispatch.statechange(getState());
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
                    d.fill = spectrum(parseFloat(d[category.title]) / category.max);
                } else {
                    d.fill = d3.rgb(150,150,150);
                }
            });

            var fills = {};
            fills["defaultFill"] = d3.rgb(150,150,150);
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
                            inner += category.display_title + ': ' + country_data[category.title];
                            if (country_data["Rank by rate of ownership"]) {
                                inner += ("<br>World Rank by Rate of Ownership: " + country_data["Rank by rate of ownership"]);
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
        var y = "Number of Homicides per 100,000 Deaths";

        var active_countries = countries.filter(function(d) {return d[x];} );
        var x_cols = [x].concat(active_countries.map(function(c) {return parseFloat(c[x]);}));

        var chart = c3.generate({
            bindto: '#bubblechart',
            data: {
                x: x,
                columns:[
                    x_cols
                ],
                type: 'scatter',
                color: function (color, d) {
                    // d will be 'id' when called for legends
                    var s = getState();
                    if (typeof(d) == 'object') {
                        var parsed = parseFloat(active_countries[d.index][s.title])/s.max;
                        if (isNaN(parsed)) {
                            return spectrum(0.5);
                        }
                        return spectrum(parsed);
                    }
                    return d3.rgb(150, 150, 150);
                }
            },
            tooltip: {
                format: {
                     title: function (x) {return "";},
                     name: function(name, ratio, id, index) {return "<div class='country-name'>" + active_countries[index]["Country"]+"</div>" + name;}
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
                },
                y: {
                    label: y
                }
            }
        });
        dispatch.on("statechange.bubblechart", function(category) {
            var y = category.title;
            // don't question the next line
            var y_title = x == y ? y + " " : y;
            $("#selection").html(getState().display_title);

            var y_cols = [y_title].concat(active_countries.map(function(c) {return parseFloat(c[y]);}));


            chart.load({
                x: x,
                columns: [
                    x_cols,
                    y_cols
                ]
            });
            var unloads = categories.map(function(c) { return c.title != y ? c.title : "";});
            if (x != y) {
                unloads.push(x + " ");
            }
            chart.unload({
                ids: unloads
            });
            chart.internal.config.axis_y_label = category.display_title;
        });
    });

    dispatch.on("load.piechart", function(countries) {
        var x = "Average total all civilian firearms";
        var cutoff = 5000000;
        var active_countries = countries.filter(function(c) {
            var parsed = parseFloat(c[x]);
            return !isNaN(parsed) && (parsed > cutoff);
        });
        var other = 0;
        var count = 0;
        countries.forEach(function(c) {
            var parsed = parseFloat(c[x]);
            if (!isNaN(parsed) && parsed <= cutoff) {
                other += parsed;
                count += 1;
            }
        });

        var columns = active_countries.map(function(c) {return [c["Country"], c[x]];});
        columns.push(["Other (" + String(count) + " countries)", other]);

        var piechart = c3.generate({
            bindto: '#piechart',
            data: {
                columns: columns,
                type: 'pie',
                color: function (color, d) {
                    // d will be 'id' when called for legends
                    var s = getState();
                    var c = getCountryData(active_countries, d.id || d);
                    try {
                        var parsed = parseFloat(c[s.title])/s.max;
                        if (isNaN(parsed)) {
                            return d3.rgb(150, 150, 150);
                        }
                        return spectrum(parsed);
                    } catch (e) {
                        return d3.rgb(150, 150, 150);
                    }
                }
            },
            legend: {
                show: false
            }
        });
        dispatch.on("statechange.piechart", function(category) {
            piechart.load({
                data: {
                    columns: columns,
                    type: 'pie'
                }
            });
        });
    });
}

function getState() {
    var id = $("input:radio[name ='radio']:checked").prop("id");
    var category;
    categories.forEach(function (c) {
        if (id == c.id) {
            category = c;
        }
    });
    if (category) {
        return category;
    }
}

$(document).ready(function() {
    var labels = "";
    categories.forEach(function(c) {
        if (c.id == categories[initial_cat].id) {
            labels += '<input type="radio" id="' + c.id + '" checked="checked" name="radio"><label for="' + c.id + '">' + c.display_title + '</label></input>';
        } else {
            labels += '<input type="radio" id="' + c.id + '" name="radio"><label for="' + c.id + '">' + c.display_title + '</label></input>';
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
        dispatch.statechange(getState());
    });
});



