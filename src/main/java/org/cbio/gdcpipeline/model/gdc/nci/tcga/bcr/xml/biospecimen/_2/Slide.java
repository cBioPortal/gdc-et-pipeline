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
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.shared._2.BcrSlideBarcode;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.shared._2.BcrSlideUuid;
import org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.shared._2.ImageFileName;
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
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}section_location"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}number_proliferating_cells"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}percent_tumor_cells"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}percent_tumor_nuclei"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}percent_normal_cells"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}percent_necrosis"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}percent_stromal_cells"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}percent_inflam_infiltration"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}percent_lymphocyte_infiltration"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}percent_monocyte_infiltration"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}percent_granulocyte_infiltration"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}percent_neutrophil_infiltration"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}percent_eosinophil_infiltration"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/shared/2.7}bcr_slide_barcode"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/shared/2.7}bcr_slide_uuid"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/shared/2.7}image_file_name" minOccurs="0"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}is_derived_from_ffpe" minOccurs="0"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}bcr_biospecimen_canonical_reasons" minOccurs="0"/>
 *         &lt;element ref="{http://tcga.nci/bcr/xml/biospecimen/2.7}type" minOccurs="0"/>
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
    "sectionLocation",
    "numberProliferatingCells",
    "percentTumorCells",
    "percentTumorNuclei",
    "percentNormalCells",
    "percentNecrosis",
    "percentStromalCells",
    "percentInflamInfiltration",
    "percentLymphocyteInfiltration",
    "percentMonocyteInfiltration",
    "percentGranulocyteInfiltration",
    "percentNeutrophilInfiltration",
    "percentEosinophilInfiltration",
    "bcrSlideBarcode",
    "bcrSlideUuid",
    "imageFileName",
    "isDerivedFromFfpe",
    "bcrBiospecimenCanonicalReasons",
    "type"
})
@XmlRootElement(name = "slide")
public class Slide {

    @XmlElement(namespace = "http://tcga.nci/bcr/xml/shared/2.7")
    protected Notes notes;
    @XmlElement(name = "additional_studies", namespace = "http://tcga.nci/bcr/xml/administration/2.7")
    protected AdditionalStudies additionalStudies;
    @XmlElement(name = "alternate_identifiers", namespace = "http://tcga.nci/bcr/xml/administration/2.7")
    protected AlternateIdentifiers alternateIdentifiers;
    @XmlElement(name = "section_location", required = true)
    protected SectionLocation sectionLocation;
    @XmlElement(name = "number_proliferating_cells", required = true)
    protected NumberProliferatingCells numberProliferatingCells;
    @XmlElement(name = "percent_tumor_cells", required = true)
    protected PercentTumorCells percentTumorCells;
    @XmlElement(name = "percent_tumor_nuclei", required = true)
    protected PercentTumorNuclei percentTumorNuclei;
    @XmlElement(name = "percent_normal_cells", required = true)
    protected PercentNormalCells percentNormalCells;
    @XmlElement(name = "percent_necrosis", required = true)
    protected PercentNecrosis percentNecrosis;
    @XmlElement(name = "percent_stromal_cells", required = true)
    protected PercentStromalCells percentStromalCells;
    @XmlElement(name = "percent_inflam_infiltration", required = true)
    protected PercentInflamInfiltration percentInflamInfiltration;
    @XmlElement(name = "percent_lymphocyte_infiltration", required = true)
    protected PercentLymphocyteInfiltration percentLymphocyteInfiltration;
    @XmlElement(name = "percent_monocyte_infiltration", required = true, nillable = true)
    protected PercentMonocyteInfiltration percentMonocyteInfiltration;
    @XmlElement(name = "percent_granulocyte_infiltration", required = true)
    protected PercentGranulocyteInfiltration percentGranulocyteInfiltration;
    @XmlElement(name = "percent_neutrophil_infiltration", required = true)
    protected PercentNeutrophilInfiltration percentNeutrophilInfiltration;
    @XmlElement(name = "percent_eosinophil_infiltration", required = true)
    protected PercentEosinophilInfiltration percentEosinophilInfiltration;
    @XmlElement(name = "bcr_slide_barcode", namespace = "http://tcga.nci/bcr/xml/shared/2.7", required = true)
    protected BcrSlideBarcode bcrSlideBarcode;
    @XmlElement(name = "bcr_slide_uuid", namespace = "http://tcga.nci/bcr/xml/shared/2.7", required = true)
    protected BcrSlideUuid bcrSlideUuid;
    @XmlElement(name = "image_file_name", namespace = "http://tcga.nci/bcr/xml/shared/2.7", nillable = true)
    protected ImageFileName imageFileName;
    @XmlElement(name = "is_derived_from_ffpe")
    protected IsDerivedFromFfpe isDerivedFromFfpe;
    @XmlElement(name = "bcr_biospecimen_canonical_reasons")
    protected BcrBiospecimenCanonicalReasons bcrBiospecimenCanonicalReasons;
    protected Type type;

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
     * Gets the value of the sectionLocation property.
     * 
     * @return
     *     possible object is
     *     {@link SectionLocation }
     *     
     */
    public SectionLocation getSectionLocation() {
        return sectionLocation;
    }

    /**
     * Sets the value of the sectionLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link SectionLocation }
     *     
     */
    public void setSectionLocation(SectionLocation value) {
        this.sectionLocation = value;
    }

    /**
     * Gets the value of the numberProliferatingCells property.
     * 
     * @return
     *     possible object is
     *     {@link NumberProliferatingCells }
     *     
     */
    public NumberProliferatingCells getNumberProliferatingCells() {
        return numberProliferatingCells;
    }

    /**
     * Sets the value of the numberProliferatingCells property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumberProliferatingCells }
     *     
     */
    public void setNumberProliferatingCells(NumberProliferatingCells value) {
        this.numberProliferatingCells = value;
    }

    /**
     * Gets the value of the percentTumorCells property.
     * 
     * @return
     *     possible object is
     *     {@link PercentTumorCells }
     *     
     */
    public PercentTumorCells getPercentTumorCells() {
        return percentTumorCells;
    }

    /**
     * Sets the value of the percentTumorCells property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentTumorCells }
     *     
     */
    public void setPercentTumorCells(PercentTumorCells value) {
        this.percentTumorCells = value;
    }

    /**
     * Gets the value of the percentTumorNuclei property.
     * 
     * @return
     *     possible object is
     *     {@link PercentTumorNuclei }
     *     
     */
    public PercentTumorNuclei getPercentTumorNuclei() {
        return percentTumorNuclei;
    }

    /**
     * Sets the value of the percentTumorNuclei property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentTumorNuclei }
     *     
     */
    public void setPercentTumorNuclei(PercentTumorNuclei value) {
        this.percentTumorNuclei = value;
    }

    /**
     * Gets the value of the percentNormalCells property.
     * 
     * @return
     *     possible object is
     *     {@link PercentNormalCells }
     *     
     */
    public PercentNormalCells getPercentNormalCells() {
        return percentNormalCells;
    }

    /**
     * Sets the value of the percentNormalCells property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentNormalCells }
     *     
     */
    public void setPercentNormalCells(PercentNormalCells value) {
        this.percentNormalCells = value;
    }

    /**
     * Gets the value of the percentNecrosis property.
     * 
     * @return
     *     possible object is
     *     {@link PercentNecrosis }
     *     
     */
    public PercentNecrosis getPercentNecrosis() {
        return percentNecrosis;
    }

    /**
     * Sets the value of the percentNecrosis property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentNecrosis }
     *     
     */
    public void setPercentNecrosis(PercentNecrosis value) {
        this.percentNecrosis = value;
    }

    /**
     * Gets the value of the percentStromalCells property.
     * 
     * @return
     *     possible object is
     *     {@link PercentStromalCells }
     *     
     */
    public PercentStromalCells getPercentStromalCells() {
        return percentStromalCells;
    }

    /**
     * Sets the value of the percentStromalCells property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentStromalCells }
     *     
     */
    public void setPercentStromalCells(PercentStromalCells value) {
        this.percentStromalCells = value;
    }

    /**
     * Gets the value of the percentInflamInfiltration property.
     * 
     * @return
     *     possible object is
     *     {@link PercentInflamInfiltration }
     *     
     */
    public PercentInflamInfiltration getPercentInflamInfiltration() {
        return percentInflamInfiltration;
    }

    /**
     * Sets the value of the percentInflamInfiltration property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentInflamInfiltration }
     *     
     */
    public void setPercentInflamInfiltration(PercentInflamInfiltration value) {
        this.percentInflamInfiltration = value;
    }

    /**
     * Gets the value of the percentLymphocyteInfiltration property.
     * 
     * @return
     *     possible object is
     *     {@link PercentLymphocyteInfiltration }
     *     
     */
    public PercentLymphocyteInfiltration getPercentLymphocyteInfiltration() {
        return percentLymphocyteInfiltration;
    }

    /**
     * Sets the value of the percentLymphocyteInfiltration property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentLymphocyteInfiltration }
     *     
     */
    public void setPercentLymphocyteInfiltration(PercentLymphocyteInfiltration value) {
        this.percentLymphocyteInfiltration = value;
    }

    /**
     * Gets the value of the percentMonocyteInfiltration property.
     * 
     * @return
     *     possible object is
     *     {@link PercentMonocyteInfiltration }
     *     
     */
    public PercentMonocyteInfiltration getPercentMonocyteInfiltration() {
        return percentMonocyteInfiltration;
    }

    /**
     * Sets the value of the percentMonocyteInfiltration property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentMonocyteInfiltration }
     *     
     */
    public void setPercentMonocyteInfiltration(PercentMonocyteInfiltration value) {
        this.percentMonocyteInfiltration = value;
    }

    /**
     * Gets the value of the percentGranulocyteInfiltration property.
     * 
     * @return
     *     possible object is
     *     {@link PercentGranulocyteInfiltration }
     *     
     */
    public PercentGranulocyteInfiltration getPercentGranulocyteInfiltration() {
        return percentGranulocyteInfiltration;
    }

    /**
     * Sets the value of the percentGranulocyteInfiltration property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentGranulocyteInfiltration }
     *     
     */
    public void setPercentGranulocyteInfiltration(PercentGranulocyteInfiltration value) {
        this.percentGranulocyteInfiltration = value;
    }

    /**
     * Gets the value of the percentNeutrophilInfiltration property.
     * 
     * @return
     *     possible object is
     *     {@link PercentNeutrophilInfiltration }
     *     
     */
    public PercentNeutrophilInfiltration getPercentNeutrophilInfiltration() {
        return percentNeutrophilInfiltration;
    }

    /**
     * Sets the value of the percentNeutrophilInfiltration property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentNeutrophilInfiltration }
     *     
     */
    public void setPercentNeutrophilInfiltration(PercentNeutrophilInfiltration value) {
        this.percentNeutrophilInfiltration = value;
    }

    /**
     * Gets the value of the percentEosinophilInfiltration property.
     * 
     * @return
     *     possible object is
     *     {@link PercentEosinophilInfiltration }
     *     
     */
    public PercentEosinophilInfiltration getPercentEosinophilInfiltration() {
        return percentEosinophilInfiltration;
    }

    /**
     * Sets the value of the percentEosinophilInfiltration property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentEosinophilInfiltration }
     *     
     */
    public void setPercentEosinophilInfiltration(PercentEosinophilInfiltration value) {
        this.percentEosinophilInfiltration = value;
    }

    /**
     * Gets the value of the bcrSlideBarcode property.
     * 
     * @return
     *     possible object is
     *     {@link BcrSlideBarcode }
     *     
     */
    public BcrSlideBarcode getBcrSlideBarcode() {
        return bcrSlideBarcode;
    }

    /**
     * Sets the value of the bcrSlideBarcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BcrSlideBarcode }
     *     
     */
    public void setBcrSlideBarcode(BcrSlideBarcode value) {
        this.bcrSlideBarcode = value;
    }

    /**
     * Gets the value of the bcrSlideUuid property.
     * 
     * @return
     *     possible object is
     *     {@link BcrSlideUuid }
     *     
     */
    public BcrSlideUuid getBcrSlideUuid() {
        return bcrSlideUuid;
    }

    /**
     * Sets the value of the bcrSlideUuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BcrSlideUuid }
     *     
     */
    public void setBcrSlideUuid(BcrSlideUuid value) {
        this.bcrSlideUuid = value;
    }

    /**
     * Gets the value of the imageFileName property.
     * 
     * @return
     *     possible object is
     *     {@link ImageFileName }
     *     
     */
    public ImageFileName getImageFileName() {
        return imageFileName;
    }

    /**
     * Sets the value of the imageFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageFileName }
     *     
     */
    public void setImageFileName(ImageFileName value) {
        this.imageFileName = value;
    }

    /**
     * Gets the value of the isDerivedFromFfpe property.
     * 
     * @return
     *     possible object is
     *     {@link IsDerivedFromFfpe }
     *     
     */
    public IsDerivedFromFfpe getIsDerivedFromFfpe() {
        return isDerivedFromFfpe;
    }

    /**
     * Sets the value of the isDerivedFromFfpe property.
     * 
     * @param value
     *     allowed object is
     *     {@link IsDerivedFromFfpe }
     *     
     */
    public void setIsDerivedFromFfpe(IsDerivedFromFfpe value) {
        this.isDerivedFromFfpe = value;
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

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link Type }
     *     
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link Type }
     *     
     */
    public void setType(Type value) {
        this.type = value;
    }

}
