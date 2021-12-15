package org.jesteban.clockomatic.store.serializers;


import org.jesteban.clockomatic.helpers.CsvSupport;
import org.jesteban.clockomatic.model.CompaniesContract;
import org.jesteban.clockomatic.model.Company;
import org.jesteban.clockomatic.store.Serializer;

import java.util.ArrayList;
import java.util.List;

public class SerializerCompaniesCsv implements Serializer<CompaniesContract> {
    private CsvSupport csv = new CsvSupport();
    private static final String CMD_CSV_INSERT_V1="I1";

    private String [] serialize(Company company){
        List<String> fields = new ArrayList<>();
        fields.add(CMD_CSV_INSERT_V1);
        fields.add(Integer.toString(company.getId()));
        fields.add(company.getName());
        fields.add(company.getDescription());
        return fields.toArray(new String[0]);
    }

    private Company deserialize(String [] fields){
        Company result = new Company();
        // TODO: Check CMD, check number of fields
        result.setId(Integer.parseInt(fields[1]));
        result.setName(fields[2]);
        result.setDescription(fields[3]);
        return result;

    }

    @Override
    public String serialize(CompaniesContract companies) throws Exception {
        List<String []> data = new ArrayList<>();
        for (Company company : companies.getAllCompaniesEnabled()){
            data.add(serialize(company));
        }
        return  csv.serialize(data);
    }

    @Override
    public void deserialize(String rawData, CompaniesContract destination) throws Exception {
        CompaniesContract result = destination;
        List<String []> data = csv.deserialize(rawData);
        for (String [] fields : data){
            result.addCompany(deserialize(fields));
        }
    }
}
