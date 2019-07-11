create index IX_2E66837D on ReportDefinition (groupId);
create index IX_9E717081 on ReportDefinition (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_DAEE2343 on ReportDefinition (uuid_[$COLUMN_LENGTH:75$], groupId);