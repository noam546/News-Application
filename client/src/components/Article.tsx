
export interface ArticleProps {
    source: Source
    author: string
    title: string
    description: string
    url: string
    urlToImage: string
    publishedAt: string
    content: string
}

export interface Source{
    id: string
    name: string
}

const Article = ({source,
                     author,
                     title,
                     description,
                     url,
                     urlToImage,
                     publishedAt,
                     content,} : ArticleProps) => {
    return (
        <div className="card mt-3">
            {urlToImage && (
                <img
                    src={urlToImage}
                    className="card-img-top img-fluid rounded"
                    alt={title}
                    style={{ maxWidth: '300px', maxHeight: '200px', objectFit: 'cover' }}
                />
            )}
            <div className="card-body">
                <h5 className="card-title">{title}</h5>
                {author && (
                    <h6 className="card-subtitle mb-2 text-muted">
                        {`By ${author}`}
                    </h6>
                )}
                <p className="card-text">{description}</p>
                <a href={url} target="_blank" rel="noopener noreferrer" className="btn btn-primary">
                    Read More on "{source.name}" website
                </a>
            </div>
            <div className="card-footer text-muted">
                Published on {new Date(publishedAt).toLocaleString()}
            </div>
        </div>
    )
}

export default Article;