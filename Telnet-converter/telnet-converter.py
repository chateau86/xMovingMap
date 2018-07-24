#python2

#convert telnet to HTTP GET reply

#Telnet at 127.0.0.1:51000 with ExtPlane
from telnetlib import Telnet

import threading
from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import SocketServer

web_port = 13338
                
global lat
global lon
global hdg
                
class ReqHandler(BaseHTTPRequestHandler):

    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()

    def do_GET(self):
        global lat
        global lon
        global hdg
        self._set_headers()
        self.wfile.write("{{lat: {:}, lon:{:}, hdg:{:}}}".format(lat,lon,hdg))

        
if __name__=='__main__':
    lat = 0
    lon = 0
    hdg = 0
    tn = Telnet('localhost', 51000)
    tn.expect([b'EXTPLANE 1'])
    tn.write(b'sub sim/flightmodel/position/latitude  0.00001'+b"\n")
    tn.write(b'sub sim/flightmodel/position/longitude 0.00001'+b"\n")
    tn.write(b'sub sim/flightmodel/position/true_psi 1'+b"\n")
    
    server_address = ('', web_port)
    httpd = HTTPServer(server_address, ReqHandler)
    server_thread = threading.Thread(target=httpd.serve_forever)
    server_thread.daemon = True
    print('starting server')
    server_thread.start()
    print('server running')
    while True:
        reply = tn.read_until(b'\n')
        #print("-"+reply+"-")
        if len(reply)>0:
            reply = str(reply)[:-3].strip().split(' ')
            if len(reply) < 2:
                continue
            if 'lat' in reply[1]:
                lat = float(reply[2])
            elif 'lon' in reply[1]:
                lon = float(reply[2])
            elif 'psi' in reply[1]:
                hdg = float(reply[2])
            print((lat, lon, hdg))