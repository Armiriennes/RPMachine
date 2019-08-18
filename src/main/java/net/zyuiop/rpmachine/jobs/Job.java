package net.zyuiop.rpmachine.jobs;

import org.bukkit.Material;

import java.util.*;

public class Job {
    protected String jobName;
    protected String jobDescription;

    // TODO: replace with 3 levels of restriction: restrictSale, restrictCraft, restrictUse (restricts use and place, for blocks only). Each inheriting the previous level(s)
    protected Set<Material> restrictSale = new HashSet<>();
    protected Set<Material> restrictCraft = new HashSet<>();
    protected Set<Material> restrictUse = new HashSet<>();
    protected Map<Material, Integer> restrictCollect = new HashMap<>();
    protected Set<JobRestrictions> restrictions = new HashSet<>();

    private Job(String jobName, String jobDescription,
               Set<Material> restrictSale,
               Set<Material> restrictCraft,
               Set<Material> restrictUse,
               Map<Material, Integer> restrictCollect,
               Set<JobRestrictions> restrictions) {
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.restrictSale.addAll(restrictSale);
        this.restrictCraft.addAll(restrictCraft);
        this.restrictUse.addAll(restrictUse);
        this.restrictions.addAll(restrictions);
        this.restrictCollect.putAll(restrictCollect);
    }

    public Set<JobRestrictions> getRestrictions() {
        return Collections.unmodifiableSet(restrictions);
    }

    public Set<Material> getRestrictSale() {
        return restrictSale;
    }

    public Set<Material> getRestrictCraft() {
        return restrictCraft;
    }

    public Set<Material> getRestrictUse() {
        return restrictUse;
    }

    public Map<Material, Integer> getRestrictCollect() {
        return restrictCollect;
    }

    public boolean canSell(Material material) {
        return restrictSale.contains(material) || canCraft(material) || canCollect(material);
    }

    public boolean canCraft(Material material) {
        return restrictCraft.contains(material) || canUse(material);
    }

    public boolean canCollect(Material material) {
        return restrictCollect.containsKey(material) || canUse(material);
    }

    public boolean canUse(Material material) {
        return restrictUse.contains(material);
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public static final class Builder {
        protected String jobName;
        protected String jobDescription;
        protected Set<Material> restrictSale = new HashSet<>();
        protected Set<Material> restrictCraft = new HashSet<>();
        protected Set<Material> restrictUse = new HashSet<>();
        protected Map<Material, Integer> restrictCollect = new HashMap<>();
        protected Set<JobRestrictions> restrictions = new HashSet<>();

        private Builder() {
        }

        public static Builder aJob() {
            return new Builder();
        }

        public Builder withJobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public Builder withJobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
            return this;
        }

        public Builder withRestrictSale(Set<Material> restrictSale) {
            this.restrictSale = restrictSale;
            return this;
        }

        public Builder withRestrictCraft(Set<Material> restrictCraft) {
            this.restrictCraft = restrictCraft;
            return this;
        }

        public Builder withRestrictUse(Set<Material> restrictUse) {
            this.restrictUse = restrictUse;
            return this;
        }

        public Builder withRestrictCollect(Map<Material, Integer> restrictCollect) {
            this.restrictCollect = restrictCollect;
            return this;
        }

        public Builder withRestrictions(Set<JobRestrictions> restrictions) {
            this.restrictions = restrictions;
            return this;
        }

        public Builder but() {
            return aJob().withJobName(jobName).withJobDescription(jobDescription).withRestrictSale(restrictSale).withRestrictCraft(restrictCraft).withRestrictUse(restrictUse).withRestrictCollect(restrictCollect).withRestrictions(restrictions);
        }

        public Job build() {
            return new Job(jobName, jobDescription, restrictSale, restrictCraft, restrictUse, restrictCollect, restrictions);
        }
    }
}
