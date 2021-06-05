# WeatherApp
Scrapes Weather Model Data and makes forecasts. The goal of this project is to scrape weather model data from a variety of weather models(Full List below), and to make accurate forecasts.

List of Weather Models that are incldued

<ol>
<li>NOAA NBM, NBE implemented, working on NBP</li>
<li>NOAA MOS(Currently Not Working)</li>
<li>HRRR</li>
<li>GFS</li>
<li>NAM CONUS</li>
<li>RAP</li>
</ol>

##June 2021 Update:
Most of the bugs have been fixed, except a small one where the NBE projections are not displayed correctly if its a specific time in the day. THe text output is pretty cluttered currently, and the user still cannot choose weather station which is set to KHIO(Hillsboro Airport, Oregon) as default. Hopefully by the end of this month all these issues will be resolved. There are a few things that I am still not extracting from the weather models that could enhance my forecasts so that will be the next step after I finish the other issues.
