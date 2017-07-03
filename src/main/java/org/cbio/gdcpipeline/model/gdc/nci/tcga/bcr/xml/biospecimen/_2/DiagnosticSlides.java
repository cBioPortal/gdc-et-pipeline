//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.20 at 07:06:08 PM IST 
//


package org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.biospecimen._2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.administration._2.AdditionalStudies;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.shared._2.Notes;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/shared/2.7}notes" minOccurs="0"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/administration/2.7}additional_studies" minOccurs="0"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}ffpe_slide_uuid" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "notes",
    "additionalStudies",
    "ffpeSlideUuid"
})
@XmlRootElement(name = "diagnostic_slides")
public class DiagnosticSlides {

    @XmlElement(namespace = "http://tcga.nci/bcr/xml/shared/2.7")
    protected Notes notes;
    @XmlElement(name = "additional_studies", namespace = "http://tcga.nci/bcr/xml/administration/2.7")
    protected AdditionalStudies additionalStudies;
    @XmlElement(name = "ffpe_slide_uuid", nillable = true)
    protected List<FfpeSlideUuid> ffpeSlideUuid;

    /**
     * Gets the value of the notes property.
     * 
     * @return
     *     possible object is
     *     {@link Notes }
     *     
     */
    public Notes getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Notes }
     *     
     */
    public void setNotes(Notes value) {
        this.notes = value;
    }

    /**
     * Gets the value of the additionalStudies property.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalStudies }
     *     
     */
    public AdditionalStudies getAdditionalStudies() {
        return additionalStudies;
    }

    /**
     * Sets the value of the additionalStudies property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalStudies }
     *     
     */
    public void setAdditionalStudies(AdditionalStudies value) {
        this.additionalStudies = value;
    }

    /**
     * Gets the value of the ffpeSlideUuid property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ffpeSlideUuid property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFfpeSlideUuid().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FfpeSlideUuid }
     * 
     * 
     */
    public List<FfpeSlideUuid> getFfpeSlideUuid() {
        if (ffpeSlideUuid == null) {
            ffpeSlideUuid = new ArrayList<FfpeSlideUuid>();
        }
        return this.ffpeSlideUuid;
    }

}