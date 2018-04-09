import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import IconButton from 'material-ui/IconButton';
import DeleteIcon from 'components/Icons/DeleteIcon';

import EntityList from 'components/EntityList';

import {
    Title,
    Container,
    DetailsTable,
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";


const TitleWrapper = glamorous.div({
    padding: '0 20px 0 0'
});

const DeleteButtonLayout = glamorous.div({
    position: 'absolute',
    top: '0',
    right: '5px'
});


const DeleteButton = glamorous(IconButton, {
    withProps: {
        style: {
            fontSize: '21px',
            color: '#a0a0a0'
        }
    }
})();


@observer
class DatasetListItem extends Component {

    constructor(props) {
        super(props);
    }

    onDelete = (e) => {
        let { dataset, onDelete } = this.props;
        e.stopPropagation();
        if(onDelete) {
            onDelete(dataset);
        }
    }

    render() {
        let { dataset, onDelete } = this.props;
        let noop = function(){};

        return (
            <Container>
                <TitleWrapper>
                    <Title>{dataset.name}</Title>
                </TitleWrapper>
                <DetailsTable>
                    {/* <DetailsTableRow>
                        <DetailsTableTitle>
                            Id:
                        </DetailsTableTitle>
                        <DetailsTableCell>{dataset.id}</DetailsTableCell>
                    </DetailsTableRow> */}
                    <DetailsTableRow>
                        <DetailsTableTitle>
                            Type:
                        </DetailsTableTitle>
                        <DetailsTableCell>{dataset.type}</DetailsTableCell>
                    </DetailsTableRow>

                    <DeleteButtonLayout>
                        <DeleteButton onClick={onDelete ? this.onDelete : noop}>
                            <DeleteIcon />
                        </DeleteButton>
                    </DeleteButtonLayout>
                    
                </DetailsTable>
            </Container>
        );
    }
}

@observer
export default class DatasetsList extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        let {
            items,
            selectedItemId,
            onRowSelection,
            onDelete
        } = this.props;
        
        return (
            <EntityList
                items={items}
                itemRenderer={item => <DatasetListItem onDelete={onDelete} dataset={item} />}
                width={'255px'}
                height={'105px'}
                selectable={true}
                selectedIds={[selectedItemId]}
                onSelect={onRowSelection}
                hideControls={true}
            />
        );
    }
}


// import Table, {
//     TableBody,
//     TableHead,
//     TableRow,
//     TableCell,
// } from "material-ui/Table";

// const StyledDatasetsList = glamorous.div({});

// const HeaderCell = glamorous(TableCell, {
//     withProps: {}
// })()

// const BodyCell = glamorous(TableCell, {
//     withProps: {}
// })()

// const BodyRow = glamorous(TableRow, {
//     withProps: ({ selected }) => ({
//         style: {background: selected ? '#cfcfcf' : 'inherit'}
//     })
// })({});

// const DatasetItem = glamorous.div({
//     marginTop: "10px",
//     marginLeft: "5px"
// });

// @observer
// class DatasetsList extends Component {
//     constructor(props) {
//         super(props);
//     }

//     onRowSelection = (dataset) => {
//         this.props.onRowSelection(dataset);
//     }

//     render() {
//         let { items, onRowSelection, selectedItemId } = this.props;
//         return (
//             <StyledDatasetsList>
//                 <Table>
//                     <TableHead>
//                         <TableRow>
//                             <HeaderCell>ID</HeaderCell>
//                             <HeaderCell>Title</HeaderCell>
//                             <HeaderCell>Type</HeaderCell>
//                         </TableRow>
//                     </TableHead>
//                     <TableBody>
//                         {items.map(dataset => {
//                             return (
//                                 <BodyRow onClick={this.onRowSelection.bind(this, dataset)} selected={selectedItemId === dataset.id} key={dataset.id}>
//                                     <BodyCell>{dataset.id}</BodyCell>
//                                     <BodyCell>{dataset.name}</BodyCell>
//                                     <BodyCell>{dataset.type}</BodyCell>
//                                 </BodyRow>
//                             );
//                         })}
//                     </TableBody>
//                 </Table>
//             </StyledDatasetsList>
//         );
//     }
// }

// export default DatasetsList;
