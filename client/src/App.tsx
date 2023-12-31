import './css/App.css'
import ArticleList from "./components/ArticleList";
import {ArticleProps} from "./components/Article";
import React, {useState, useEffect} from 'react';
import {fetchArticlesByDetails, fetchTopArticles} from './api';


const INITIAL_ARTICLES_NUMBER: number = 20;

function App() {

    const [articles, setArticles] = useState<ArticleProps[]>([]);
    const [searchText, setSearchText] = useState<String | undefined>("hapoel jerusalem");
    const [startIndex, setStartIndex] = useState<number>(0);
    const [endIndex, setEndIndex] = useState<number>(INITIAL_ARTICLES_NUMBER);
    const [showMoreButton, setShowMoreButton] = useState<boolean>(false);
    const [fromDate, setFromDate] = useState<String | undefined>(undefined);
    const [toDate, setToDate] = useState<String | undefined>(undefined);
    const [fetchingArticlesError, setFetchingArticlesError] = useState<String | undefined>(undefined);
    //hook
    useEffect(() => {

        const fetchData = async () => {
            const response = await fetchArticlesByDetails(searchText, fromDate, toDate, startIndex, endIndex);
            handleResponse(response);
        };
        fetchData();
    }, [searchText, fromDate, toDate, startIndex, endIndex, showMoreButton]);

    const handleResponse = (response: { status: number, data?: ArticleProps[], error?: string }) => {
        if (response.status === 200 && response.data) {
            //in case we are fetching more article of the same search
            if (startIndex != 0) {
                if (response.data.length === 0)
                    setShowMoreButton(false)
                else {
                    if (response.data.length < 20)
                        setShowMoreButton(false)
                    const updatedArticles = articles?.concat(response.data);
                    setArticles(updatedArticles)
                }
            } else {
                if (response.data.length === 0 || response.data.length < 20)
                    setShowMoreButton(false)
                else
                    setShowMoreButton(true)
                setArticles(response.data);
            }
            setFetchingArticlesError(undefined);
        } else {
            setStartIndex(0)
            setEndIndex(INITIAL_ARTICLES_NUMBER)
            setShowMoreButton(false)
            setFetchingArticlesError(response.error);
        }
    }


    const handleSearchClick = () => {
        // @ts-ignore
        const newSearchValue = document.getElementById('search').value;
        // @ts-ignore
        const fromDate = document.getElementById('datepicker-from').value;
        // @ts-ignore
        const toDate = document.getElementById('datepicker-to').value;
        if (isIllegalDates(fromDate,toDate)) {
            setFetchingArticlesError("Dates values are illegal. from-date cannot be later from to-date")
        } else if (newSearchValue.length > 0) {
            setSearchText(newSearchValue);
            setStartIndex(0);
            setEndIndex(20);
            setFromDate(fromDate)
            setToDate(toDate)
            setShowMoreButton(true);
        }
    }

    const isIllegalDates = (from: string, to: string) => {
        const startDate: Date = new Date(from);
        const endDate: Date = new Date(to);
        if(endDate < startDate)
            return true;
        return false;
    }

    const handleLoadMoreClick = () => {
        setStartIndex(startIndex + 20)
        setEndIndex(endIndex + 20)
    }

    return (
        <div className="container">
            <header className="App-header">
                <h1>Welcome to the best news website ever</h1>
            </header>
            <main>
                <nav className="navbar navbar-expand-lg bg-body-tertiary">

                    <div className="text-container">
                        <label></label>
                        <input id="search" type="text" className="form-control" placeholder="Search"
                               aria-label="Username"/>
                    </div>

                    <div className="input-container">
                        <label className="date-label">from date</label>
                        <input type="date" id="datepicker-from" className="form-control" min="2023-10-28"/>
                    </div>

                    <div className="input-container">
                        <label className="date-label">to date</label>
                        <input type="date" id="datepicker-to" className="form-control" min="2023-10-28"/>
                    </div>

                    <div className="button-container">
                        <label></label>
                        <button className="btn btn-outline-success" type="submit" onClick={handleSearchClick}>
                            Search
                        </button>
                    </div>

                </nav>

                {fetchingArticlesError ?
                    <div className="error-message-container">
                        <h3 className="error-message">⚠️{fetchingArticlesError}⚠️</h3>
                    </div> : <ArticleList articles={articles}/>}
                {showMoreButton && searchText && searchText.length > 0 && <div className="button-container">
                    <button className="btn btn-outline-success" type="submit" onClick={handleLoadMoreClick}>
                        more
                    </button>
                </div>}

            </main>
        </div>
    )
}

export default App
