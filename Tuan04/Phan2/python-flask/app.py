from flask import Flask
app = Flask(__name__)

@app.route('/')
def home():
    return "Hello, Docker Flask!"

app.run(host='0.0.0.0', port=5000)

# docker build -t python-flask .
# docker run -p 5000:5000 python-flask