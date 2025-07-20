from flask import Flask, jsonify, request
import pandas as pd
import numpy as np

# Load DataFrame and similarity matrix
df = pd.read_csv('songs_data.csv')
similar = np.load('similarity_matrix.npy')

# Create Flask app
app = Flask(__name__)

# Recommender function
def recommender(song_name):
    try:
        idx = df[df["song_name"] == song_name].index[0]
        distance = sorted(list(enumerate(similar[idx])), reverse=True, key=lambda x: x[1])
        
        # Collect the top 4 similar songs with their track information
        recommendations = []
        for s_id in distance[1:5]:
            song_data = df.iloc[s_id[0]]
            recommendations.append({
                "song_name": song_data.song_name,
                "track_id": song_data.track_id  # or track_id if using that
            })
        
        return recommendations
    except IndexError:
        return f"Song '{song_name}' not found."

# API endpoint for recommendations
@app.route('/recommend', methods=['GET'])
def recommend_songs():
    song_name = request.args.get('song')
    
    if not song_name:
        return jsonify({"error": "Please provide a song name"}), 400
    
    # Get recommended songs
    similar_songs = recommender(song_name)
    
    if isinstance(similar_songs, str):  # if error message
        return jsonify({"error": similar_songs}), 404
    
    return jsonify({"song": song_name, "recommendations": similar_songs})

# Run the Flask app
if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)