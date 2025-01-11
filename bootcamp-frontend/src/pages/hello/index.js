import axios from "axios";


export default function HelloPage() {

    // axios GET Products

    // axios.request({
    //     method: 'GET',
    //     url: 'http://localhost:8081/users/csekas@ctrlspace.dev',
    //     headers: {
    //         'Content-Type': 'application/json',
    //         'Authorization': 'Basic'
    //     }});

    return (
        <>
            <div className={"bordered"}>
                <h1>Hello!!!!!!!</h1>
                <p>This is a paragraph....</p>

                <h2>This is a sub title</h2>
            </div>

            <div className={"important, bordered"}>
                <p>This is a Unordered list</p>
                <ul>
                    <li>Item 1</li>
                    <li>Item 2</li>
                    <li>Item 3</li>
                </ul>

                <p> This is an ordered list</p>
                <ol>
                    <li>Item 1</li>
                    <li>Item 2</li>
                    <li>Item 3</li>
                </ol>
            </div>

            <p className={"bordered"}> This is a table</p>
            <table className={"important, bordered"}>
                <thead>
                <tr>
                    <th>Header 1</th>
                    <th>Header 2</th>
                    <th>Header 3</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Row 1, Cell 1</td>
                    <td>Row 1, Cell 2</td>
                    <td>Row 1, Cell 3</td>
                </tr>
                <tr>
                    <td>Row 2, Cell 1</td>
                    <td>Row 2, Cell 2</td>
                    <td>Row 2, Cell 3</td>
                </tr>
                <tr>
                    <td>Row 3, Cell 1</td>
                    <td>Row 3, Cell 2</td>
                    <td>Row 3, Cell 3</td>
                </tr>
                </tbody>
            </table>

            <p> This is a form</p>
            <form>
                <label for="name">Name:</label>
                <input type="text" id="name" name="name"></input>
                <br></br>
                <label for="email">Email:</label>
                <input type="email" id="email" name="email"></input>
                <br></br>
                <label for="password">Password:</label>
                <input type="password" id="password" name="password"></input>
                <br></br>
                <input type="submit" value="Submit"></input>
            </form>

            <p> This is a link</p>
            <a href="https://www.google.com">Google</a>
            <p>This is an image</p>
            <img src="https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
                 alt="Google Logo"></img>


        </>
    );
}