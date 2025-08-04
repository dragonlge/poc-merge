import { Table, Space, Modal, Button, Divider, Card } from "antd";
import { useLoaderData, useParams } from "react-router";
import { useState } from "react";
import PocEmployeeList from "./PocEmployeeList";
import { fetchPocEmployee, bindingEmployee } from "../service/api";
import JsonView from "@uiw/react-json-view";

const columnWhiteList = [
  "id",
  "remoteId",
  "employeeNumber",
  "lastName",
  "preferredName",
  "displayFullName",
  "username",
  "workEmail",
  "mobilePhoneNumber",
];

const HRISEmployeeList = () => {
  const { clientId } = useParams();
  const dataSource = useLoaderData();
  const [horizonEmployee, setHorizonEmployee] = useState([]);

  const [hrisEmployeeId, setHrisEmployeeId] = useState(null);
  const [horizonEmployeeRecord, setHorizonEmployeeRecord] = useState(null);
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [rawDataModal, setRawDataModal] = useState(false);
  const [jsonData, setJsonData] = useState(null);

  if (dataSource.length == 0) {
    return <p>No data</p>;
  }

  // const [horizonEmployeeId, setHorizonEmployeeId] = useState(null);

  let firstRow = columnWhiteList;
  if (dataSource) {
    firstRow = dataSource[0];
  }
  const columns = Object.keys(firstRow)
    .filter((key) => columnWhiteList.includes(key))
    .map((key) => {
      return {
        title: key,
        dataIndex: key,
        key,
      };
    });
  columns.push({
    title: "RawData",
    key: "id",
    render: (_, record) => (
      <Space size="middle">
        <a
          onClick={() => {
            setRawDataModal(true);
            setJsonData(JSON.parse(record.rawData));
          }}
        >
          RawData
        </a>
      </Space>
    ),
  });

  columns.push({
    title: "Action",
    key: "id",
    render: (_, record) => (
      <Space size="middle">
        <a
          onClick={() => {
            setHrisEmployeeId(record.id);
            showLoading();
          }}
        >
          Binding
        </a>
      </Space>
    ),
  });

  // modal
  const showLoading = async () => {
    setOpen(true);
    setLoading(true);
    const response = await fetchPocEmployee(clientId);
    setLoading(false);
    setHorizonEmployee(response);
  };
  return (
    <>
      <p>HRIS Employee List</p>
      <Divider />
      <Table dataSource={dataSource} columns={columns} rowKey="id" />

      <Modal
        title={<p>Binding HRIS employee with Horizon employee</p>}
        footer={
          <Button
            type="primary"
            onClick={async () => {
              await bindingEmployee({
                integration_employee_id: hrisEmployeeId,
                horizon_employee_id: horizonEmployeeRecord.employee_id,
                horizon_user_id: horizonEmployeeRecord.user_id,
              });
              setHrisEmployeeId(null);
              setHorizonEmployeeRecord(null);
              setOpen(false);
            }}
          >
            Bind
          </Button>
        }
        loading={loading}
        open={open}
        onCancel={() => {
          setHrisEmployeeId(null);
          setOpen(false);
          setHorizonEmployeeRecord(null);
        }}
      >
        <PocEmployeeList
          initialDataSource={horizonEmployee}
          onSelect={setHorizonEmployeeRecord}
        />
      </Modal>
      <Modal
        title="Raw data"
        open={rawDataModal}
        onOk={() => {
          setRawDataModal(false);
          setJsonData(null);
        }}
        onCancel={() => {
          setRawDataModal(false);
          setJsonData(null);
        }}
      >
        <Card title="JSON Data Preview">
          <JsonView value={jsonData} />
        </Card>
      </Modal>
    </>
  );
};
export default HRISEmployeeList;
