import React, { Component } from "react";
import glamorous from "glamorous";

const StyledTitle = glamorous.div({
    padding: "18px 78px 15px 20px",
    fontSize: "16px",
    textTransform: "uppercase",
    fontWeight: "500",
    color: "#01579b",
    overflow: "hidden",
    textOverflow: "ellipsis",
    whiteSpace: "nowrap",
    borderBottom: "1px solid #ccc"
});

const Title = ({ children: text }) => {
    return <StyledTitle title={text}>{text}</StyledTitle>;
};

const DetailsTable = glamorous.div({
    padding: "15px 20px"
});

const DetailsTableRow = glamorous.div({
    padding: "5px 0px",
    whiteSpace: "nowrap",
    overflow: "hidden",
    textOverflow: "ellipsis"
});

const DetailsTableCell = ({ children: text }) => {
    return (
        <span title={Array.isArray(text) ? text.filter(t => !!t)[0] : text}>
            {text}
        </span>
    );
};

//const DetailsTableCell = glamorous.span({});

const DetailsTableTitle = glamorous.span({
    fontWeight: "500",
    marginRight: "5px",
    display: "inline-block"
});

const Container = glamorous.div({
    padding: "0",
    fontSize: "14px"
});

export {
    Title,
    Container,
    DetailsTable,
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
};
