//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.13 at 06:01:48 AM IST 
//


package org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.pharmaceutical._2;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://tcga.nci/bcr/xml/utility/2.7>generic_day">
 *       &lt;attGroup ref="{http://tcga.nci/bcr/xml/utility/2.7}common_ext_attribute_group"/>
 *       &lt;attribute name="cde" type="{http://www.w3.org/2001/XMLSchema}string" default="3103070" />
 *       &lt;attribute name="xsd_ver" type="{http://www.w3.org/2001/XMLSchema}string" default="1.12" />
 *       &lt;attribute name="tier" type="{http://www.w3.org/2001/XMLSchema}string" default="2" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "value"
})
@XmlRootElement(name = "day_of_drug_therapy_start")
public class DayOfDrugTherapyStart {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "cde")
    protected String cde;
    @XmlAttribute(name = "xsd_ver")
    protected String xsdVer;
    @XmlAttribute(name = "tier")
    protected String tier;
    @XmlAttribute(name = "cde_ver")
    protected String cdeVer;
    @XmlAttribute(name = "procurement_status", required = true)
    protected String procurementStatus;
    @XmlAttribute(name = "owner", required = true)
    protected String owner;
    @XmlAttribute(name = "precision")
    protected String precision;
    @XmlAttribute(name = "preferred_name")
    protected String preferredName;
    @XmlAttribute(name = "display_order")
    protected BigInteger displayOrder;
    @XmlAttribute(name = "restricted")
    protected Boolean restricted;
    @XmlAttribute(name = "source_system_identifier")
    protected BigInteger sourceSystemIdentifier;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the cde property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCde() {
        if (cde == null) {
            return "3103070";
        } else {
            return cde;
        }
    }

    /**
     * Sets the value of the cde property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCde(String value) {
        this.cde = value;
    }

    /**
     * Gets the value of the xsdVer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXsdVer() {
        if (xsdVer == null) {
            return "1.12";
        } else {
            return xsdVer;
        }
    }

    /**
     * Sets the value of the xsdVer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXsdVer(String value) {
        this.xsdVer = value;
    }

    /**
     * Gets the value of the tier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTier() {
        if (tier == null) {
            return "2";
        } else {
            return tier;
        }
    }

    /**
     * Sets the value of the tier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTier(String value) {
        this.tier = value;
    }

    /**
     * Gets the value of the cdeVer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdeVer() {
        if (cdeVer == null) {
            return "";
        } else {
            return cdeVer;
        }
    }

    /**
     * Sets the value of the cdeVer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdeVer(String value) {
        this.cdeVer = value;
    }

    /**
     * Gets the value of the procurementStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcurementStatus() {
        return procurementStatus;
    }

    /**
     * Sets the value of the procurementStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcurementStatus(String value) {
        this.procurementStatus = value;
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwner(String value) {
        this.owner = value;
    }

    /**
     * Gets the value of the precision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrecision() {
        return precision;
    }

    /**
     * Sets the value of the precision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrecision(String value) {
        this.precision = value;
    }

    /**
     * Gets the value of the preferredName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreferredName() {
        if (preferredName == null) {
            return "";
        } else {
            return preferredName;
        }
    }

    /**
     * Sets the value of the preferredName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreferredName(String value) {
        this.preferredName = value;
    }

    /**
     * Gets the value of the displayOrder property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDisplayOrder() {
        if (displayOrder == null) {
            return new BigInteger("9999");
        } else {
            return displayOrder;
        }
    }

    /**
     * Sets the value of the displayOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDisplayOrder(BigInteger value) {
        this.displayOrder = value;
    }

    /**
     * Gets the value of the restricted property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isRestricted() {
        if (restricted == null) {
            return false;
        } else {
            return restricted;
        }
    }

    /**
     * Sets the value of the restricted property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRestricted(Boolean value) {
        this.restricted = value;
    }

    /**
     * Gets the value of the sourceSystemIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSourceSystemIdentifier() {
        return sourceSystemIdentifier;
    }

    /**
     * Sets the value of the sourceSystemIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSourceSystemIdentifier(BigInteger value) {
        this.sourceSystemIdentifier = value;
    }

}
