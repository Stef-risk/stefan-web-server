#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';

use DBI;
use CGI(":standard");
use CGI::Carp("fatalsToBrowser");

print("Content-type: text/html\n\n");

my $studentId=param('studentId');
# connect to MySQL...
my $driver= "mysql";
my $dsn = "DBI:$driver:database=student_database;host=";
my $dbh = DBI->connect($dsn, "stefrisk", "123456");

# prepare and execute the SQL statement
my $sth = $dbh->prepare("SELECT stu_name,stu_class
       FROM student
       WHERE stu_id=$studentId");
$sth->execute;

# retrieve the results
print("studentId:$studentId");
print()