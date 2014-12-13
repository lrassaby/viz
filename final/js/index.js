// www.theguardian.com/news/datablog/2012/jul/22/gun-homicides-ownership-world-list
// http://en.wikipedia.org/wiki/List_of_countries_by_intentional_homicide_rate
var categories = [
{"id": "homicides", "title": "Homicides per 100,000", "max": 90.4},
{"id": "firearm-homicides", "title": "Homicide by firearm rate per 100,000", "max": 68.43},
{"id": "firearms-per-100", "title": "Average firearms per 100 people", "max": 88.8},
{"id": "percent-homicides", "title": "% of homicides by firearm", "max": 100}];

var initial_cat = 0;
var dispatch;

function start() {
    dispatch = d3.dispatch("load", "statechange");

    d3.csv("data/morbid.csv", function(error, countries) {
        if (error) throw error;
        var countriesById = d3.map();
        // key value store such that ALB: "Albania", ALB, 100 etc.
        countries.forEach(function(d) {
            d.fillKey = d["Code"];
            countriesById.set(d["Code"], d);
        });
       
        dispatch.load(countriesById);
        dispatch.statechange(categories[initial_cat]);
    });

    dispatch.on("load.map", function(countriesById) {

        dispatch.on("statechange.map", function(category) {
            $("#map").empty();

            countriesById.forEach(function(c) {
                var d = countriesById.get(c);
                if (d[category.title]) {
                    d.fill = "rgba(138,7,7," + String((parseFloat(d[category.title]) / category.max) + 0.2) + ")";
                } else {
                    d.fill = "rgba(200,200,200, 0.3)";
                }
            });

            var fills = {};
            countriesById.forEach(function(d) {
                fills[d] = countriesById.get(d).fill;
            });
            $("#map").datamaps({
                fills: fills,
                data: countriesById._,
                geographyConfig: {
                    popupTemplate: function(geo, data) {
                        var inner = "<div class='country-name'>" + geo.properties.name + "</div>";
                        if (data) {
                            inner += category.title + ': ' + data[category.title];
                            // display rank, total numbers
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