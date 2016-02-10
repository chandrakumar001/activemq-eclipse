ActiveMQ Eclipse Uses Eclipse-3.4 (Ganymede)

## Required ##

  * Get [Eclipse 3.4](http://www.eclipse.org/). Select one of versions as list:
  1. Eclipse IDE for Java EE Developers:http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/ganymede/R/eclipse-jee-ganymede-win32.zip".
  1. Eclipse Classic 3.4:http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops/R-3.4-200806172000/eclipse-SDK-3.4-win32.zip.

  * Get Draw2D&GEF&ZEST. "GEF3.4.0 All-In-One SDK (Runtime, Source, Examples)":http://www.eclipse.org/downloads/download.php?file=/tools/gef/downloads/drops/3.4.0/R200806091334/GEF-ALL-3.4.0.zip.

  * Get the latest [Source](http://code.google.com/p/camel-route-viewer/source/checkout).

  * JDK1.5.

## Run ##

Open plugin.xml,in the testing tab select "Lanuch an Eclipse Application".

In the runtime workbench, open the following views

  * **ActiveMQ Connections**
  * **ActiveMQ Network View**.

Run a broker locally

Right click on a connection in the connections view and select **connect**.

You should now see the network graph in the ActiveMQ Network view