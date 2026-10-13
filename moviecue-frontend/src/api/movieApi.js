const BASE_URL = `${import.meta.env.VITE_API_URL}/api`;

function getVisitorId() {
    let id = localStorage.getItem('moviecueVisitorId');
    if (!id) {
        id = crypto.randomUUID();
        localStorage.setItem('moviecueVisitorId', id);
    }
    return id;
}

async function request(path, options = {}) {
    const res = await fetch(`${BASE_URL}${path}`, {
        ...options,
        headers: { ...(options.headers || {}), 'X-Visitor-Id': getVisitorId()},
    });
    if (!res.ok) {
        let message = 'Request failed.';
        try {
            const data = await res.json();
            if (data.error) message = data.error;
        } catch {

        }
        throw new Error(message);
    }
    if (res.status === 204) {
        return null;
    }
    return res.json();
}

export async function searchMovie(title) {
    return request(`/movies/search?title=${encodeURIComponent(title)}`);
}

export function getMyRatings() {
    return request(`/ratings`);
}

export async function rateMovie(movieId, score) {
    return request(`/ratings?movieId=${movieId}&score=${score}`, { method: `POST`});
}

export async function deleteRating(ratingId) {
    return request(`/ratings/${ratingId}`, { method: `DELETE`});
}

export async function getRecommendations(limit = 5) {
    return request(`/recommendations/for-me?limit=${limit}`);
}