//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.20 at 07:06:08 PM IST 
//


package org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.biospecimen._2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.administration._2.AdditionalStudies;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.administration._2.AlternateIdentifiers;
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
 *         &lt;element ref="{http://tcga.nci/bcr/xml/administration/2.7}alternate_identifiers" minOccurs="0"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}portion_number"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}portion_sequence" minOccurs="0"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}plate_id"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}center_id"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}shipment_portion_day_of_shipment"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}shipment_portion_month_of_shipment"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}shipment_portion_year_of_shipment"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}shipment_portion_bcr_aliquot_barcode"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}bcr_shipment_portion_uuid"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}is_ffpe" minOccurs="0"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}bcr_biospecimen_canonical_reasons" minOccurs="0"/>
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
    "alternateIdentifiers",
    "portionNumber",
    "portionSequence",
    "plateId",
    "centerId",
    "shipmentPortionDayOfShipment",
    "shipmentPortionMonthOfShipment",
    "shipmentPortionYearOfShipment",
    "shipmentPortionBcrAliquotBarcode",
    "bcrShipmentPortionUuid",
    "isFfpe",
    "bcrBiospecimenCanonicalReasons"
})
@XmlRootElement(name = "shipment_portion")
public class ShipmentPortion {

    @XmlElement(namespace = "http://tcga.nci/bcr/xml/shared/2.7")
    protected Notes notes;
    @XmlElement(name = "additional_studies", namespace = "http://tcga.nci/bcr/xml/administration/2.7")
    protected AdditionalStudies additionalStudies;
    @XmlElement(name = "alternate_identifiers", namespace = "http://tcga.nci/bcr/xml/administration/2.7")
    protected AlternateIdentifiers alternateIdentifiers;
    @XmlElement(name = "portion_number", required = true, nillable = true)
    protected PortionNumber portionNumber;
    @XmlElement(name = "portion_sequence", nillable = true)
    protected PortionSequence portionSequence;
    @XmlElement(name = "plate_id", required = true)
    protected PlateId plateId;
    @XmlElement(name = "center_id", required = true)
    protected CenterId centerId;
    @XmlElement(name = "shipment_portion_day_of_shipment", required = true)
    protected ShipmentPortionDayOfShipment shipmentPortionDayOfShipment;
    @XmlElement(name = "shipment_portion_month_of_shipment", required = true)
    protected ShipmentPortionMonthOfShipment shipmentPortionMonthOfShipment;
    @XmlElement(name = "shipment_portion_year_of_shipment", required = true)
    protected ShipmentPortionYearOfShipment shipmentPortionYearOfShipment;
    @XmlElement(name = "shipment_portion_bcr_aliquot_barcode", required = true)
    protected ShipmentPortionBcrAliquotBarcode shipmentPortionBcrAliquotBarcode;
    @XmlElement(name = "bcr_shipment_portion_uuid", required = true, nillable = true)
    protected BcrShipmentPortionUuid bcrShipmentPortionUuid;
    @XmlElement(name = "is_ffpe")
    protected IsFfpe isFfpe;
    @XmlElement(name = "bcr_biospecimen_canonical_reasons")
    protected BcrBiospecimenCanonicalReasons bcrBiospecimenCanonicalReasons;

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
     * Gets the value of the alternateIdentifiers property.
     * 
     * @return
     *     possible object is
     *     {@link AlternateIdentifiers }
     *     
     */
    public AlternateIdentifiers getAlternateIdentifiers() {
        return alternateIdentifiers;
    }

    /**
     * Sets the value of the alternateIdentifiers property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlternateIdentifiers }
     *     
     */
    public void setAlternateIdentifiers(AlternateIdentifiers value) {
        this.alternateIdentifiers = value;
    }

    /**
     * Gets the value of the portionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link PortionNumber }
     *     
     */
    public PortionNumber getPortionNumber() {
        return portionNumber;
    }

    /**
     * Sets the value of the portionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link PortionNumber }
     *     
     */
    public void setPortionNumber(PortionNumber value) {
        this.portionNumber = value;
    }

    /**
     * Gets the value of the portionSequence property.
     * 
     * @return
     *     possible object is
     *     {@link PortionSequence }
     *     
     */
    public PortionSequence getPortionSequence() {
        return portionSequence;
    }

    /**
     * Sets the value of the portionSequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link PortionSequence }
     *     
     */
    public void setPortionSequence(PortionSequence value) {
        this.portionSequence = value;
    }

    /**
     * Gets the value of the plateId property.
     * 
     * @return
     *     possible object is
     *     {@link PlateId }
     *     
     */
    public PlateId getPlateId() {
        return plateId;
    }

    /**
     * Sets the value of the plateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlateId }
     *     
     */
    public void setPlateId(PlateId value) {
        this.plateId = value;
    }

    /**
     * Gets the value of the centerId property.
     * 
     * @return
     *     possible object is
     *     {@link CenterId }
     *     
     */
    public CenterId getCenterId() {
        return centerId;
    }

    /**
     * Sets the value of the centerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link CenterId }
     *     
     */
    public void setCenterId(CenterId value) {
        this.centerId = value;
    }

    /**
     * Gets the value of the shipmentPortionDayOfShipment property.
     * 
     * @return
     *     possible object is
     *     {@link ShipmentPortionDayOfShipment }
     *     
     */
    public ShipmentPortionDayOfShipment getShipmentPortionDayOfShipment() {
        return shipmentPortionDayOfShipment;
    }

    /**
     * Sets the value of the shipmentPortionDayOfShipment property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShipmentPortionDayOfShipment }
     *     
     */
    public void setShipmentPortionDayOfShipment(ShipmentPortionDayOfShipment value) {
        this.shipmentPortionDayOfShipment = value;
    }

    /**
     * Gets the value of the shipmentPortionMonthOfShipment property.
     * 
     * @return
     *     possible object is
     *     {@link ShipmentPortionMonthOfShipment }
     *     
     */
    public ShipmentPortionMonthOfShipment getShipmentPortionMonthOfShipment() {
        return shipmentPortionMonthOfShipment;
    }

    /**
     * Sets the value of the shipmentPortionMonthOfShipment property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShipmentPortionMonthOfShipment }
     *     
     */
    public void setShipmentPortionMonthOfShipment(ShipmentPortionMonthOfShipment value) {
        this.shipmentPortionMonthOfShipment = value;
    }

    /**
     * Gets the value of the shipmentPortionYearOfShipment property.
     * 
     * @return
     *     possible object is
     *     {@link ShipmentPortionYearOfShipment }
     *     
     */
    public ShipmentPortionYearOfShipment getShipmentPortionYearOfShipment() {
        return shipmentPortionYearOfShipment;
    }

    /**
     * Sets the value of the shipmentPortionYearOfShipment property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShipmentPortionYearOfShipment }
     *     
     */
    public void setShipmentPortionYearOfShipment(ShipmentPortionYearOfShipment value) {
        this.shipmentPortionYearOfShipment = value;
    }

    /**
     * Gets the value of the shipmentPortionBcrAliquotBarcode property.
     * 
     * @return
     *     possible object is
     *     {@link ShipmentPortionBcrAliquotBarcode }
     *     
     */
    public ShipmentPortionBcrAliquotBarcode getShipmentPortionBcrAliquotBarcode() {
        return shipmentPortionBcrAliquotBarcode;
    }

    /**
     * Sets the value of the shipmentPortionBcrAliquotBarcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShipmentPortionBcrAliquotBarcode }
     *     
     */
    public void setShipmentPortionBcrAliquotBarcode(ShipmentPortionBcrAliquotBarcode value) {
        this.shipmentPortionBcrAliquotBarcode = value;
    }

    /**
     * Gets the value of the bcrShipmentPortionUuid property.
     * 
     * @return
     *     possible object is
     *     {@link BcrShipmentPortionUuid }
     *     
     */
    public BcrShipmentPortionUuid getBcrShipmentPortionUuid() {
        return bcrShipmentPortionUuid;
    }

    /**
     * Sets the value of the bcrShipmentPortionUuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BcrShipmentPortionUuid }
     *     
     */
    public void setBcrShipmentPortionUuid(BcrShipmentPortionUuid value) {
        this.bcrShipmentPortionUuid = value;
    }

    /**
     * Gets the value of the isFfpe property.
     * 
     * @return
     *     possible object is
     *     {@link IsFfpe }
     *     
     */
    public IsFfpe getIsFfpe() {
        return isFfpe;
    }

    /**
     * Sets the value of the isFfpe property.
     * 
     * @param value
     *     allowed object is
     *     {@link IsFfpe }
     *     
     */
    public void setIsFfpe(IsFfpe value) {
        this.isFfpe = value;
    }

    /**
     * Gets the value of the bcrBiospecimenCanonicalReasons property.
     * 
     * @return
     *     possible object is
     *     {@link BcrBiospecimenCanonicalReasons }
     *     
     */
    public BcrBiospecimenCanonicalReasons getBcrBiospecimenCanonicalReasons() {
        return bcrBiospecimenCanonicalReasons;
    }

    /**
     * Sets the value of the bcrBiospecimenCanonicalReasons property.
     * 
     * @param value
     *     allowed object is
     *     {@link BcrBiospecimenCanonicalReasons }
     *     
     */
    public void setBcrBiospecimenCanonicalReasons(BcrBiospecimenCanonicalReasons value) {
        this.bcrBiospecimenCanonicalReasons = value;
    }

}
