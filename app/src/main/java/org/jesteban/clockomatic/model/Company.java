package org.jesteban.clockomatic.model;


public class Company {
    public static final int COLOR_NOT_SET = 0;
    String name = "NONE";
    String description = "Nothing";
    int id = -1;
    int color = COLOR_NOT_SET;
    Boolean isEnabled = true;
    int defaultWorkSchedule = -1;

    public Company() {

    }

    public Company(Company c) {
        this(c.getId(), c.getName(), c.getDescription(), c.isEnabled(), c.getColor());
    }

    public Company(String pname, String pdescription) {
        this(-1, pname, pdescription, true);
    }

    public Company(int pid, String pname, String pdescription) {
        this(pid, pname, pdescription, true);
    }

    public Company(int pid, String pname, String pdescription, Boolean state) {
        this(pid, pname, pdescription, state, COLOR_NOT_SET);
    }

    public Company(int pid, String pname, String pdescription, Boolean state, int color) {
        setId(pid);
        setName(pname);
        setDescription(pdescription);
        setIsEnabled(state);
        setColor(color);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean v) {
        isEnabled = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDefaultWorkSchedule() { return this.defaultWorkSchedule;}

    public void setDefaultWorkSchedule(int ws) { this.defaultWorkSchedule = ws;}

    public String toString() {
        return "COMPANY[id=" + Integer.toString(getId()) + " name=" + getName() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (id != company.id) return false;
        if (name != null ? !name.equals(company.name) : company.name != null) return false;
        if (description != null ? !description.equals(company.description) : company.description != null)
            return false;
        return isEnabled != null ? isEnabled.equals(company.isEnabled) : company.isEnabled == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (isEnabled != null ? isEnabled.hashCode() : 0);
        return result;
    }
}
