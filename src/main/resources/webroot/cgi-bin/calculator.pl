use strict;
use warnings FATAL => 'all';

use CGI qw(:standard);
use CGI::Carp("fatalsToBrowser");
print "Content-type: text/html\n\n";

my $firstNumber=param('number1');
my $secondNumber=param('number2');

my $result=$firstNumber+$secondNumber;

print "<h2>result:$result</h2>";