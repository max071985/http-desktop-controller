from audio_controller import Singleton
import psutil
import os
import json


class WindowsController(object, metaclass=Singleton):
    def __init__(self):
        pass

    @staticmethod
    def get_processes():
        # Iterate over all running process using psutil
        output = []
        for proc in psutil.process_iter():
            try:
                # Get process name & pid from process object.
                process_name = proc.name()
                process_id = proc.pid
                print(process_name, ' ::: ', process_id)  # debug
                output.append({"process_name": process_name, "process_id": process_id})
            except (psutil.NoSuchProcess, psutil.AccessDenied, psutil.ZombieProcess):
                pass
        return json.dumps(output)

    @staticmethod
    def shutdown():
        os.system("shutdown /s /t 1")

    @staticmethod
    def restart():
        os.system("shutdown /r /t 1")
