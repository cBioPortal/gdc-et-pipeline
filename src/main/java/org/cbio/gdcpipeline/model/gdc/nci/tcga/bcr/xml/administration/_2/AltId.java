//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.13 at 06:01:48 AM IST 
//


package org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.administration._2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.utility._2.AltIdType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>NCName">
 *       &lt;attGroup ref="{http://tcga.nci/bcr/xml/administration/2.7}admin_res_attribute_group"/>
 *       &lt;attribute name="alt_id_type" type="{http://tcga.nci/bcr/xml/utility/2.7}alt_id_type" />
 *       &lt;attribute name="project_code" type="{http://www.w3.org/2001/XMLSchema}string" />
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
@XmlRootElement(name = "alt_id")
public class AltId {

    @XmlValue
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String value;
    @XmlAttribute(name = "alt_id_type")
    protected AltIdType altIdType;
    @XmlAttribute(name = "project_code")
    protected String projectCode;
    @XmlAttribute(name = "xsd_ver")
    protected String xsdVer;

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
     * Gets the value of the altIdType property.
     * 
     * @return
     *     possible object is
     *     {@link AltIdType }
     *     
     */
    public AltIdType getAltIdType() {
        return altIdType;
    }

    /**
     * Sets the value of the altIdType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AltIdType }
     *     
     */
    public void setAltIdType(AltIdType value) {
        this.altIdType = value;
    }

    /**
     * Gets the value of the projectCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * Sets the value of the projectCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjectCode(String value) {
        this.projectCode = value;
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
            return "";
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

}