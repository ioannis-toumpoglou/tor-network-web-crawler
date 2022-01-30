# Tor-network-web-crawler

This is a Java version of the application that was developed for my Master's degree thesis in 2019 
https://github.com/ioannis-toumpoglou/tor-web-crawler

It is used for crawling webpages that belong to the Tor network, also known as hidden services. 
Both the entire HTML code and the extracted text of each page is stored in an SQL database for further processing. 

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
