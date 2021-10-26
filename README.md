# Tor-network-web-crawler

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
