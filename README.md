<img src="https://zenmo.com/wp-content/uploads/elementor/thumbs/zenmo-logo-website-light-grey-square-o1piz2j6llwl7n0xd84ywkivuyf22xei68ewzwrvmc.png" height="30px"/> ZEnMo Zero
==========

Web-based application to model and simulate local energy systems.

Components
----------

### Frontend

Graphical user interface.

### Kleinverbruik

Stedin, Liander and Enexis publish usage data of natural gas and electricity anually.
This is a small webservice which wraps this data so the frontend can request small parts of the data.

### Ztor

This is the base backend built on Ktor framework. Like AnyLogic it leans on the Java ecosystem.
