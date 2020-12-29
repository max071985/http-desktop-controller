from http.server import BaseHTTPRequestHandler, HTTPServer
import urllib.parse as urlparse
from enum import Enum
from audio_controller import *
from windows_controller import WindowsController
import sys


class Requests(Enum):
    NONE = 0x0
    # Volume Control
    VOLUME_MUTE = 0x01
    VOLUME_UNMUTE = 0x02
    VOLUME_UP = 0x03
    VOLUME_DOWN = 0x04
    VOLUME_SET = 0x05
    VOLUME_GET = 0x06
    AUDIO_SESSIONS = 0x07

    # PC Control
    PC_Shutdown = 0x10
    PC_Restart = 0x11
    PC_Get_Processes = 0x12


class HTTPServer_helper(object):

    def __init__(self, port=8080):
        try:
            self.server = ('', port)
            self.httpd = HTTPServer(self.server, RequestHandler)
            print("Starting service at ", self.server)
            self.httpd.serve_forever()
        except:
            pass


class RequestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        request_arguments = {}
        # query string parsing
        try:
            if "?" in self.path:
                for key, value in dict(urlparse.parse_qsl(self.path.split("?")[1], True)).items():
                    request_arguments[key] = value
            result = self.__process_request(request_arguments)
            # Finish request stuff
            self.send_response(200)
            self.end_headers()
            self.wfile.write(result.encode())
        except:
            print("Exception raised in do_GET")  # debug
            self.send_error(501)
            self.end_headers()
            self.wfile.write(b"failed to generate a response.")

    def __process_request(self, args):
        if len(args) < 1:
            return "Not enough arguments provided."
        # Get request arguments
        if "code" in args:
            try:
                code = int(args["code"], 16)
            except ValueError:
                code = 0x0
                print("Failed to convert code argument.")
        if "process" in args:
            process = args["process"]
        if "value" in args:
            try:
                value = float(args["value"])
            except ValueError:
                value = 0
                print("Failed to convert value argument.")

        # Initialize controllers
        if 0x0 < code < 0x10:  # if code is audio related - initialize audio controller
            audio_controller = AudioController(process)
        elif 0x10 <= code < 0x20:  # if code is computer control related.
            # space to initialize windows stuff if needed.
            pass

        # Process request by codes:
        # Volume Codes:
        if code == Requests.VOLUME_DOWN.value:
            print(code)  # debug
            audio_controller.decrease_volume(value)
            return f"{process} volume was turned down by {value}."
        elif code == Requests.VOLUME_UP.value:
            print(code)  # debug
            audio_controller.increase_volume(value)
            return f"{process} volume was turned up by {value}."
        elif code == Requests.VOLUME_GET.value:
            print(code)  # debug
            return audio_controller.process_volume()
        elif code == Requests.VOLUME_SET.value:
            print(code)  # debug
            audio_controller.set_volume(value)
            return "{1} volume set to {0}".format(value, process)
        elif code == Requests.VOLUME_MUTE.value:
            print(code)  # debug
            audio_controller.mute()
            return f"{process} was muted."
        elif code == Requests.VOLUME_UNMUTE.value:
            print(code)  # debug
            audio_controller.unmute()
            return f"{process} was un-muted."
        elif code == Requests.AUDIO_SESSIONS.value:
            print(code)  # debug
            sessions = AudioController.get_audio_sessions()
            output = ""
            for session in sessions:
                output += "PID: {0} ::: Display name: {1} ::: Volume: {2}\n".format(str(session.ProcessId),
                                                                                    str(session.DisplayName),
                                                                                    str(
                                                                                        session.SimpleAudioVolume))
            return output
        # PC Control Codes: TODO: Add password to confirm shutdown
        elif code == Requests.PC_Shutdown.value:
            WindowsController.shutdown()
            return "Shutting the computer down."
        elif code == Requests.PC_Restart.value:
            WindowsController.restart()
            return "Restarting the computer."
        elif code == Requests.PC_Get_Processes.value:
            return WindowsController.get_processes()
        else:
            print("INVALID CODE:", code)  # debug
        print(args)  # debug
        return "Invalid request code."
