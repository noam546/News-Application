const API_BASE_URL = 'http://localhost:8080/api/v1/news';


export const fetchArticlesByDetails = async (searchText?: String, fromDate?: String, toDate?: String, startIndex?: number, endIndex?: number) => {
    const url = createEverythingUrl(searchText, fromDate, toDate, startIndex, endIndex)
    return await sendHttpRequest(url)
};

export const fetchTopArticles = async () => {
    const url = API_BASE_URL + "/top-headlines"
    return await sendHttpRequest(url);
};

const sendHttpRequest = async (url: string) => {
    try {
        const response = await fetch(url);

        switch (response.status) {
            case 200:
                const data = await response.json();
                return {
                    status: response.status,
                    data: data,
                };
            case 400:
                return {
                    status: response.status,
                    error: "Bad search try something else"
                };
            case 401:
                return {
                    status: response.status,
                    error: "You have no credentials for this search"
                };
            case 429:
                return {
                    status: response.status,
                    error: "You tried too many times in short period. Please try later"
                };
            default :
                return {
                    status: response.status,
                    error: "Internal server error"
                };
        }

    } catch (error) {

        return {
            status: 500,
            error: 'Internal Server Error',
        };
    }
}
const createEverythingUrl = (searchText?: String, fromDate?: String, toDate?: String, startIndex?: number, endIndex?: number) => {
    let url = API_BASE_URL + "/everything"
    let first = true;
    if (searchText) {
        url += `?searchText=${searchText}`;
        first = false;
    }
    if (fromDate) {
        url += `${first ? '?' : '&'}fromDate=${fromDate}`;
        first = false;
    }
    if (toDate) {
        url += `${first ? '?' : '&'}toDate=${toDate}`;
        first = false;
    }
    if (startIndex) {
        url += `${first ? '?' : '&'}startIndex=${startIndex}`;
        first = false;
    }
    if (endIndex) {
        url += `${first ? '?' : '&'}endIndex=${endIndex}`;
    }
    return url
}