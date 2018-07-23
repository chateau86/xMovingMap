#python3

#convert telnet to HTTP GET reply

#Telnet at 127.0.0.1:51000 with ExtPlane
from telnetlib import Telnet

with Telnet('localhost', 51000) as tn:
    tn.expect([b'EXTPLANE'])
    tn.write(b'sub sim/flightmodel/position/latitude  0.00001'+b"\n")
    tn.write(b'sub sim/flightmodel/position/longitude 0.00001'+b"\n")
    tn.write(b'sub sim/cockpit2/gauges/indicators/compass_heading_deg_mag 1'+b"\n")
    while True:
        reply = tn.read_until(b'\n')
        if reply != b'':
            print(reply)