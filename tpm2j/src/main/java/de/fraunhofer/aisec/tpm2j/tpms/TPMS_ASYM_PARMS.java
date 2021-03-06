package de.fraunhofer.aisec.tpm2j.tpms;

import de.fraunhofer.aisec.tpm2j.tools.ByteArrayReadWriter;
import de.fraunhofer.aisec.tpm2j.tools.ByteArrayUtil;
import de.fraunhofer.aisec.tpm2j.tpmt.TPMT_ASYM_SCHEME;
import de.fraunhofer.aisec.tpm2j.tpmt.TPMT_SYM_DEF_OBJECT;
import de.fraunhofer.aisec.tpm2j.tpmu.TPMU_PUBLIC_PARMS;

public class TPMS_ASYM_PARMS extends TPMU_PUBLIC_PARMS {
	
	/*
	 * TPMS_ASYM_PARMS Structure
	 * typedef struct {
	 *     TPMT_SYM_DEF_OBJECT symmetric;
	 *     TPMT_ASYM_SCHEME    scheme;
	 * } TPMS_ASYM_PARMS;
	 */
	
	private TPMT_SYM_DEF_OBJECT symmetric;
	private TPMT_ASYM_SCHEME scheme;

	public TPMS_ASYM_PARMS() {
	}
	
	public TPMS_ASYM_PARMS(byte[] buffer) throws Exception {
		this.fromBytes(buffer, 0);
	}
	
	public TPMT_SYM_DEF_OBJECT getSymmetric() {
		return symmetric;
	}

	public void setSymmetric(TPMT_SYM_DEF_OBJECT symmetric) {
		this.symmetric = symmetric;
	}

	public TPMT_ASYM_SCHEME getScheme() {
		return scheme;
	}

	public void setScheme(TPMT_ASYM_SCHEME scheme) {
		this.scheme = scheme;
	}

	@Override
	public byte[] getBuffer() {
		return this.toBytes();
	} 
	
	@Override
	public byte[] toBytes() {
		return ByteArrayUtil.buildBuf(symmetric, scheme);
		        
	}	

	@Override
	public void fromBytes(byte[] source, int offset) throws Exception {
		ByteArrayReadWriter brw = new ByteArrayReadWriter( source, offset );
        this.symmetric = new TPMT_SYM_DEF_OBJECT();
        brw.readStruct(this.symmetric);
        this.scheme = new TPMT_ASYM_SCHEME();
        brw.readStruct(this.scheme);
	}

	@Override
    public String toString() {
        return "TPMS_ASYM_PARMS:[size=" + this.symmetric.toString() 
        	+ ", publicArea=" + this.scheme.toString() + "]";
    }

}
