# Cultured
Don't just stay up to date with what's going on in the world, know why it's happening.

##Table of Contents:
* [Introduction](#introduction)
* [Design](#design)

## <a name="introduction"></a>Introduction

Don't you want to know what's going on in the world but don't want to read lengthy news articles? Cultured solves that.

Select an article that interests you and from there you'll be able to learn more about the people, organizations, and countries involved.  

Happy Travels.

## <a name="design"></a>Design

<i>Cultured</i> is a relatively small Android app with it's strengths lying in its clean architecture, acheived though the use of libraries provided by Square.

The user is presented with a list of news articles provided by New York Times's public API.  
These articles are fetched and their associated images downloaded asynchronously, having Retrofit 2 handling the calls to network.  

Upon viewing an article the user has a viewpager full of wikipedia articls, each shown within a webview, that provide more information on the people, places, and organizations involved in the articles.
They have the option to view the article's abstract or they can view the entire article outside of the app by clicking an icon.
<br>
<br>
<br>
<br>
![get-it-on-google-play-logo-300x89](https://cloud.githubusercontent.com/assets/7444521/14589801/ff9a92b0-04b8-11e6-85f7-6284126d8d05.png)
