# Tor-network-web-crawler

This is a Java version of the <a href="https://github.com/ioannis-toumpoglou/tor-web-crawler">application</a> that was developed for my Master's degree thesis in 2019.


It is a web crawler capable of collecting data from webpages that belong to the Tor network, also known as hidden services. 
Both the entire HTML code and the extracted text of each page are stored in an SQL database for further processing. 

The application uses MySQL database in the backend and a user named **dbuser** is required.<br>
In order to create this user, the following commands should be executed, using either **MySQL Workbench**
or **MySQL shell**:

1. CREATE USER 'dbuser'@'localhost' IDENTIFIED BY 'dbuser';
2. GRANT ALL PRIVILEGES ON * . * TO 'dbuser'@'localhost';
3. FLUSH PRIVILEGES;
<br><br>

For the application to connect to the Tor Network, it is necessary to have the **Tor browser** 
open at the same time.<br>
To download the Tor browser, please use this <a href="https://www.torproject.org/download/">link</a>.

An unsupervised machine learing algorithm is used in order to classify the extracted text, based on the cyber-threat type it refers to. It is implemented in Python and the script is called and executed via the Spring Boot main app, right after the page's text is stored in the database.

The machine learning algorithm used is <b>K-Means</b>.
