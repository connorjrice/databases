#!/usr/bin/python

import cgi
from random import randint
import cgitb; cgitb.enable()

form = cgi.FieldStorage()

import MySQLdb



conn = MySQLdb.connect (host = "localhost",
                        user = "root",
                        passwd = "password",
                        db = "connor")
num = randint(0,10000)
cursor = conn.cursor()
cursor.execute("SELECT * FROM pictures right join users on pictures.creatorid = users.id WHERE pictures.id = %s;" % num)
rows = cursor.fetchall()

fixed = num
while (fixed > 100):
    fixed /= 10

print "Content-type:text/html\r\n\r\n"
print'<html>'
print '<head>'
print '<link rel="stylesheet" type="text/css" href="http://cs.coloradocollege.edu:/~cp344/cr/tostr/styles/main.css">'
print '<div class="image">'
print '''<img src="%s" alt="" />''' % ("http://cs.coloradocollege.edu/~cp344/cr/tostr/images/" + str(fixed) + ".jpg")

print '</img></div>'
print '<p><h3>Title: %s | Likes: %s</p><p>Artist: %s | Date: %s</h3></p>' % (rows[0][4], rows[0][2], rows[0][6], rows[0][3])
print '</head>'
print'</html>'

cursor.close()
conn.close()








