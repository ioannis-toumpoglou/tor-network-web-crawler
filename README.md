# Tor-network-web-crawler

The application uses a MySQL database in the backend. A user named <b>dbuser</b> is required.<br>
In order to create this user the following commands should be executed:

1. CREATE USER 'dbuser'@'localhost' IDENTIFIED BY 'dbuser';
2. GRANT ALL PRIVILEGES ON * . * TO 'dbuser'@'localhost';
3. FLUSH PRIVILEGES;
<br>