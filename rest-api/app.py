import json

from bson import json_util
from flask import Flask, request

from mongo import read_from_mongo

app = Flask(__name__)

@app.route('/items', methods=['GET'])
def get_items():
    subreddit = request.args.get('subreddit')
    from_timestamp = request.args.get('from')
    to_timestamp = request.args.get('to')
    keyword = request.args.get('keyword')

    if subreddit is None or from_timestamp is None or to_timestamp is None:
        return "ERROR"

    if int(from_timestamp) > int(to_timestamp):
        return "Error: from_timestamp > to_timestamp"

    docs = read_from_mongo(subreddit, from_timestamp, to_timestamp, keyword)
    return json.dumps(docs, default=json_util.default)
    return "ok"

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
