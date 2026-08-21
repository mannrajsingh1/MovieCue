const BASE_URL = 'http://localhost:8080/api';

export async function searchMovie(title) {
    const res = await fetch(`${BASE_URL}/movies/search?title=${encodeURIComponent(title)}`);
    if (!res.ok) {
        throw new Error('Movie not found.');
    }
    return res.json();
}

export async function getAllMovies() {
    const res = await fetch(`${BASE_URL}/movies`);
    return res.json();
}

export async function rateMovie(movieId, score) {
    const res = await fetch(`${BASE_URL}/ratings?movieId=${movieId}&score=${score}`, { method: 'POST',});
    if (!res.ok) {
        throw new Error('Failed to rate movie.');
    }
    return res.json();
}

export async function getRecommendations(limit = 5) {
    const res = await fetch(`${BASE_URL}/recommendations/for-me?limit=${limit}`);

    if (!res.ok) {
        throw new Error('Failed to fetch recommendations.');
    }
    return res.json();
}