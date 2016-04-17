from flask import Flask, request, redirect
import twilio.twiml
 
app = Flask(__name__)

@app.route("/message", methods=['POST'])
def message():
	from_number = request.values.get("From", None)
	message = request.values.get("Message", None)
	



@app.route("/success", methods=['GET')
def success():
	resp = twilio.twiml.Response()
	resp.message("Hello, Your request has been queued.")
	return str(resp)

@app.route("/error", methods=['GET'])
def error():
	resp = twilio.twiml.Response()
	resp.message("Hello, your request could not be processed. Try again, later.")
	return str(resp)

@app.route("/", methods=['GET', 'POST'])
def hello():
	resp = twilio.twiml.Response()
	resp.message("Hello, welcome to Pir8-Rdio! Message us your requests at +4915735987286")
	return str(resp)
                    
if __name__ == "__main__":
	app.run()
