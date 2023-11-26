import React from 'react';
import Article, {ArticleProps} from './Article';

interface ArticleListProps {
    articles: ArticleProps[];
}
const ArticleList : React.FC<ArticleListProps> = ({ articles }) => {
    return (
        <div>
            {articles.map((article, index) => (
                <Article key={index} {...article} />
            ))}
        </div>
    );
};

export default ArticleList;
