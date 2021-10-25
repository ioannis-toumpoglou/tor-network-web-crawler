import mysql.connector
from mysql.connector import Error
from time import sleep


def get_record(url):
    try:
        connection = mysql.connector.connect(host='localhost',
                                             database='tor_web_crawler',
                                             user='dbuser',
                                             password='dbuser',
                                             port=3306)
        if connection.is_connected():
            cursor = connection.cursor()
            cursor.execute("SELECT * FROM web_page_content WHERE url=%s", (url,))
            data = cursor.fetchone()
        connection.close()
        return data
            
    except Error as e:
        print("Error while connecting to MySQL", e)
        
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()
            print("MySQL connection is closed")


def insert_threat_type(url, threat_type):
    try:
        connection = mysql.connector.connect(host='localhost',
                                             database='tor_web_crawler',
                                             user='dbuser',
                                             password='dbuser',
                                             port=3306)
        if connection.is_connected():
            cursor = connection.cursor()
            cursor.execute("UPDATE web_page_content SET threat_type=%s WHERE url=%s", (threat_type,url))
            connection.commit()
            connection.close()
            
    except Error as e:
        print("Error while connecting to MySQL", e)
        
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()
            print("MySQL connection is closed")


def page_content():
    try:
        connection = mysql.connector.connect(host='localhost',
                                             database='tor_web_crawler',
                                             user='dbuser',
                                             password='dbuser',
                                             port=3306)
        if connection.is_connected():
            cursor = connection.cursor()
            cursor.execute("SELECT * FROM web_page_content")
            rows = cursor.fetchall()
        connection.close()
        return rows
    
    except Error as e:
        print("Error while connecting to MySQL", e)
        
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()
            print("MySQL connection is closed")
