export function isAuthenticated() {
    return !!localStorage.getItem('token')
}

export function logout() {
    localStorage.removeItem('token')
    window.location.href = '/'
}

export function getToken() {
    return localStorage.getItem('token')
}
