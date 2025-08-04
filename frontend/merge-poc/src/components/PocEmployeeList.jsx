import { Table, Divider } from "antd";
import { useEffect, useState } from "react";
import { useParams } from "react-router";
import { fetchPocEmployee } from "../service/api";

const columnWhiteList = ["user_id", "employee_id", "employee_name"];

const PocEmployeeList = ({ initialDataSource, onSelect }) => {
  const [dataSource, setDataSource] = useState(initialDataSource);
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const { clientId } = useParams();

  useEffect(() => {
    const fetchData = async () => {
      if (!dataSource) {
        const resp = await fetchPocEmployee(clientId);
        setDataSource(resp);
      }
    };

    fetchData();
  });

  const rowSelection = {
    type: "radio", // Specify radio selection
    selectedRowKeys,
    onChange: (selectedKeys, selectedRows) => {
      console.log(
        `selectedRowKeys: ${selectedKeys}`,
        "selectedRows: ",
        selectedRows
      );
      setSelectedRowKeys(selectedKeys); // Update the state with the new selected key
      onSelect(selectedRows[0]);
    },
  };

  const columns = columnWhiteList.map((key) => {
    return {
      title: key,
      dataIndex: key,
      key,
    };
  });
  return (
    <>
      {!onSelect && (
        <>
          <p>Poc Employee List</p>
          <Divider />
        </>
      )}
      <Table
        rowSelection={onSelect ? rowSelection : null}
        dataSource={dataSource}
        columns={columns}
        rowKey="employee_id"
      />
    </>
  );
};
export default PocEmployeeList;
