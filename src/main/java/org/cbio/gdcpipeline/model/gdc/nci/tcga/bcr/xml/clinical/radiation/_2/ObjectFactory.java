//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.13 at 06:01:48 AM IST 
//


package org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.radiation._2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.radiation._2 package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CourseNumber_QNAME = new QName("http://tcga.nci/bcr/xml/clinical/radiation/2.7", "course_number");
    private final static QName _Numfractions_QNAME = new QName("http://tcga.nci/bcr/xml/clinical/radiation/2.7", "numfractions");
    private final static QName _RadiationDosage_QNAME = new QName("http://tcga.nci/bcr/xml/clinical/radiation/2.7", "radiation_dosage");
    private final static QName _RadiationDosageMetastasis_QNAME = new QName("http://tcga.nci/bcr/xml/clinical/radiation/2.7", "radiation_dosage_metastasis");
    private final static QName _RadiationTreatmentOngoing_QNAME = new QName("http://tcga.nci/bcr/xml/clinical/radiation/2.7", "radiation_treatment_ongoing");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.cbio.gdcpipeline.model.gdc.nci.tcga.bcr.xml.clinical.radiation._2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Radiations }
     * 
     */
    public Radiations createRadiations() {
        return new Radiations();
    }

    /**
     * Create an instance of {@link Radiation }
     * 
     */
    public Radiation createRadiation() {
        return new Radiation();
    }

    /**
     * Create an instance of {@link BcrRadiationBarcode }
     * 
     */
    public BcrRadiationBarcode createBcrRadiationBarcode() {
        return new BcrRadiationBarcode();
    }

    /**
     * Create an instance of {@link BcrRadiationUuid }
     * 
     */
    public BcrRadiationUuid createBcrRadiationUuid() {
        return new BcrRadiationUuid();
    }

    /**
     * Create an instance of {@link DayOfRadiationTherapyStart }
     * 
     */
    public DayOfRadiationTherapyStart createDayOfRadiationTherapyStart() {
        return new DayOfRadiationTherapyStart();
    }

    /**
     * Create an instance of {@link MonthOfRadiationTherapyStart }
     * 
     */
    public MonthOfRadiationTherapyStart createMonthOfRadiationTherapyStart() {
        return new MonthOfRadiationTherapyStart();
    }

    /**
     * Create an instance of {@link YearOfRadiationTherapyStart }
     * 
     */
    public YearOfRadiationTherapyStart createYearOfRadiationTherapyStart() {
        return new YearOfRadiationTherapyStart();
    }

    /**
     * Create an instance of {@link DaysToRadiationTherapyStart }
     * 
     */
    public DaysToRadiationTherapyStart createDaysToRadiationTherapyStart() {
        return new DaysToRadiationTherapyStart();
    }

    /**
     * Create an instance of {@link DayOfRadiationTherapyEnd }
     * 
     */
    public DayOfRadiationTherapyEnd createDayOfRadiationTherapyEnd() {
        return new DayOfRadiationTherapyEnd();
    }

    /**
     * Create an instance of {@link MonthOfRadiationTherapyEnd }
     * 
     */
    public MonthOfRadiationTherapyEnd createMonthOfRadiationTherapyEnd() {
        return new MonthOfRadiationTherapyEnd();
    }

    /**
     * Create an instance of {@link YearOfRadiationTherapyEnd }
     * 
     */
    public YearOfRadiationTherapyEnd createYearOfRadiationTherapyEnd() {
        return new YearOfRadiationTherapyEnd();
    }

    /**
     * Create an instance of {@link DaysToRadiationTherapyEnd }
     * 
     */
    public DaysToRadiationTherapyEnd createDaysToRadiationTherapyEnd() {
        return new DaysToRadiationTherapyEnd();
    }

    /**
     * Create an instance of {@link PriorRadiationTypeMetastasis }
     * 
     */
    public PriorRadiationTypeMetastasis createPriorRadiationTypeMetastasis() {
        return new PriorRadiationTypeMetastasis();
    }

    /**
     * Create an instance of {@link PriorRadiationTypeNotesMetastasis }
     * 
     */
    public PriorRadiationTypeNotesMetastasis createPriorRadiationTypeNotesMetastasis() {
        return new PriorRadiationTypeNotesMetastasis();
    }

    /**
     * Create an instance of {@link RadiationDosageMetastasis }
     * 
     */
    public RadiationDosageMetastasis createRadiationDosageMetastasis() {
        return new RadiationDosageMetastasis();
    }

    /**
     * Create an instance of {@link RadiationType }
     * 
     */
    public RadiationType createRadiationType() {
        return new RadiationType();
    }

    /**
     * Create an instance of {@link RadiationTypeNotes }
     * 
     */
    public RadiationTypeNotes createRadiationTypeNotes() {
        return new RadiationTypeNotes();
    }

    /**
     * Create an instance of {@link RadiationDosage }
     * 
     */
    public RadiationDosage createRadiationDosage() {
        return new RadiationDosage();
    }

    /**
     * Create an instance of {@link Units }
     * 
     */
    public Units createUnits() {
        return new Units();
    }

    /**
     * Create an instance of {@link Numfractions }
     * 
     */
    public Numfractions createNumfractions() {
        return new Numfractions();
    }

    /**
     * Create an instance of {@link AnatomicTreatmentSite }
     * 
     */
    public AnatomicTreatmentSite createAnatomicTreatmentSite() {
        return new AnatomicTreatmentSite();
    }

    /**
     * Create an instance of {@link RadiationTreatmentOngoing }
     * 
     */
    public RadiationTreatmentOngoing createRadiationTreatmentOngoing() {
        return new RadiationTreatmentOngoing();
    }

    /**
     * Create an instance of {@link CourseNumber }
     * 
     */
    public CourseNumber createCourseNumber() {
        return new CourseNumber();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CourseNumber }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcga.nci/bcr/xml/clinical/radiation/2.7", name = "course_number")
    public JAXBElement<CourseNumber> createCourseNumber(CourseNumber value) {
        return new JAXBElement<CourseNumber>(_CourseNumber_QNAME, CourseNumber.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Numfractions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcga.nci/bcr/xml/clinical/radiation/2.7", name = "numfractions")
    public JAXBElement<Numfractions> createNumfractions(Numfractions value) {
        return new JAXBElement<Numfractions>(_Numfractions_QNAME, Numfractions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RadiationDosage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcga.nci/bcr/xml/clinical/radiation/2.7", name = "radiation_dosage")
    public JAXBElement<RadiationDosage> createRadiationDosage(RadiationDosage value) {
        return new JAXBElement<RadiationDosage>(_RadiationDosage_QNAME, RadiationDosage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RadiationDosageMetastasis }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcga.nci/bcr/xml/clinical/radiation/2.7", name = "radiation_dosage_metastasis")
    public JAXBElement<RadiationDosageMetastasis> createRadiationDosageMetastasis(RadiationDosageMetastasis value) {
        return new JAXBElement<RadiationDosageMetastasis>(_RadiationDosageMetastasis_QNAME, RadiationDosageMetastasis.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RadiationTreatmentOngoing }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tcga.nci/bcr/xml/clinical/radiation/2.7", name = "radiation_treatment_ongoing")
    public JAXBElement<RadiationTreatmentOngoing> createRadiationTreatmentOngoing(RadiationTreatmentOngoing value) {
        return new JAXBElement<RadiationTreatmentOngoing>(_RadiationTreatmentOngoing_QNAME, RadiationTreatmentOngoing.class, null, value);
    }

}
