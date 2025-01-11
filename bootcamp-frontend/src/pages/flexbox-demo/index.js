import axios from "axios";


export default function FlexboxPage() {

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
            <div className={"pageContainer"}>
                <div className={"pageItem"}>
                    1
                </div>
                <div className={"pageItem"}>
                    2
                </div>
                <div className={"pageItem"}>
                    3
                </div>
                <div className={"pageItem"}>
                    4
                </div>
                <div className={"pageItem"}>
                    5
                </div>
                <div className={"pageItem"}>
                    6
                </div>

            </div>
        </>
    );
}