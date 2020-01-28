import flask
from flask import request,jsonify
app = flask.Flask(__name__)
@app.route('/', methods=['POST'])
def incomingMessage():
    phone_no=request.form['phone_no']
    Message=request.form['Message']
    print(phone_no)
    print(Message)
    #Now you have got the sender's number and the message.Store it.. etc
    return "Task Done"

if __name__ == "__main__":
    app.run()






