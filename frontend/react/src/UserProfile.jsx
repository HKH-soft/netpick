const UserProfile = ({name, header, theme , ...props}) => {
    return (
        <div>
            <h1>{header}</h1>
            <p>{name}</p>
            <img 
                src={`https://react.dev/_next/image?url=%2Fimages%2Fdocs%2Fdiagrams%2Fwriting_jsx_html.${theme}.png&w=384&q=75`} />
            {props.children}
        </div>
    )
}

export default UserProfile