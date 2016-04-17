from flask import Flask, request, redirect
import twilio.twiml
 
app = Flask(__name__)

@app.route("/success", methods=['GET', 'POST'])
def success():
	from_number = request.values.get("From", None)
	resp = twilio.twiml.Response()
	resp.message("Hello, Your request has been queued.")
	return str(resp)

@app.route("/error", methods=['GET', 'POST'])
def error():
	resp = twilio.twiml.Response()
	resp.message("Hello, your request could not be processed. Try again, later.")
	return str(resp)
                    
if __name__ == "__main__":
	app.run(debug=True)
