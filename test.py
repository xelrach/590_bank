#!/usr/bin/env python
import optparse
import subprocess
import os

def main():
    # Parse command-line arguments
    p = optparse.OptionParser()
    p.add_option('--topology', '-t', default="TOPO", help="Name of the topology file.  Must be in the same directory as test.py.")
    options, arguments = p.parse_args()
    
    # Make
    p_make = subprocess.Popen(['make'], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    output,errors = p_make.communicate()
    print output
    print errors
    
    # Branch server/GUI startup
    print '*** Start server and read topology file: ***'
    p1 = subprocess.Popen(['java', 'topology', options.topology], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    output, errors = p1.communicate()
    print output
    print errors
    
    

if __name__ == '__main__':
    main()