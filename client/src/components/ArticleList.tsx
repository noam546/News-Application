import React from 'react';
import Article, {ArticleProps} from './Article';

interface ArticleListProps {
    articles: ArticleProps[];
}
const ArticleList : React.FC<ArticleListProps> = ({ articles }) => {
    return (
        <div>
            {/*<h2 className="mt-4 mb-4">Article List</h2>*/}
            {articles.map((article, index) => (
                <Article key={index} {...article} />
            ))}
        </div>
    );
};

export default ArticleList;
