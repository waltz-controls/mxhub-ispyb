/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************************************************************/

package ispyb.server.common.hdf5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5ScalarDS;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;

public class HDF5FileReader {

	private final static Logger LOGGER = Logger.getLogger(HDF5FileReader.class);
	private final String filepath;

	public HDF5FileReader(String HDF5FilePath) {
		this.filepath = HDF5FilePath;
		LOGGER.info("filepath = " + filepath);		
	}

	private String getThreeColumnStringByKeys(int frame, String valueColum, String StdColumn, Group root) throws Exception {
		HashMap<String, ArrayList<String>> result = this.getH5FrameScattering(frame, true, true, root);
		StringBuilder sb = new StringBuilder();
		if (result.containsKey("q")) {
			if (result.containsKey(valueColum)) {
				if (result.containsKey(StdColumn)) {
					sb.append("# File generated by ISPyB from " + new File(this.filepath).getName());
					sb.append("\n");
					sb.append("# Frame number " + frame);
					sb.append("\n# ");
					sb.append(GregorianCalendar.getInstance().getTime().toString());
					sb.append("\n");
					int size = result.get("q").size();
					for (int i = 0; i < size; i++) {
						sb.append(result.get("q").get(i));
						sb.append("\t");
						sb.append(result.get(valueColum).get(i));
						sb.append("\t");
						sb.append(result.get(StdColumn).get(i));
						sb.append("\n");
					}
				}
			}
		}
		return sb.toString();
	}
	
	private String getThreeColumnStringByPeakKeys(String framesRange, List<List<String>> peakData) throws Exception {
//		HashMap<String, ArrayList<String>> result = this.getH5FrameScattering(frame, true, true, root);
		StringBuilder sb = new StringBuilder();
//		if (result.containsKey("q")) {
					sb.append("# File generated by ISPyB from " + new File(this.filepath).getName());
					sb.append("\n");
					sb.append("# Peak average " + framesRange);
					sb.append("\n# ");
					sb.append(GregorianCalendar.getInstance().getTime().toString());
					sb.append("\n");
//					int size = result.get("q").size();
					for (int i = 0; i < peakData.size(); i++) {
						sb.append(peakData.get(i).get(0));
						sb.append("\t");
						sb.append(peakData.get(i).get(1));
						sb.append("\t");
						sb.append(peakData.get(i).get(2));
						sb.append("\n");
					}
//		}
		return sb.toString();
	}
	

	private String getH5FrameScatteringFile(int frame, Group root) throws Exception {
		return this.getThreeColumnStringByKeys(frame, "scattering_I", "scattering_Stdev", root);
	}

	private String getH5FrameSubtractionFile(int frame, Group root) throws Exception {
		return this.getThreeColumnStringByKeys(frame, "subtracted_I", "subtracted_Stdev", root);
	}

	private String getH5PeakAverageFile(int peak, Group root) throws Exception {
		return this.getThreeColumnStringByKeys(peak, "merge_I", "merge_Stdev", root);
	}

	private String getH5FrameAverageBufferFile(int frame, Group root) throws Exception {
		return this.getThreeColumnStringByKeys(frame, "buffer_I", "buffer_Stdev", root);
	}

	public String getH5FrameScatteringFile(int frame) throws Exception {
		FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
		FileFormat h5File = fileFormat.open(filepath, FileFormat.READ);
		if (h5File == null) {
			throw new Exception("Failed to open file: " + filepath);
		}
		String result;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			h5File.open();
			Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File.getRootNode()).getUserObject();
			result = this.getThreeColumnStringByKeys(frame, "scattering_I", "scattering_Stdev", root);
		} catch (Exception e) {
			throw e;
		} finally {
			h5File.close();
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	public byte[] getH5ZipFileByteArrayByFrameRange(int start, int end) throws Exception {
		if (start < 0)
			return null;
		if (start > end)
			return null;

		FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
		FileFormat h5File = fileFormat.open(filepath, FileFormat.READ);
		if (h5File == null) {
			throw new Exception("Failed to open file: " + filepath);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			h5File.open();
			Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File.getRootNode()).getUserObject();
			ZipOutputStream zipfile = new ZipOutputStream(bos);

			float[] scattering_I = getDataSetDataFromH5FileByName(root, "scattering_I");
			/** Point depends on q **/
			HashMap<String, float[]> resultQ = this.getQ(root);
			int pointsNumber = resultQ.get("q").length;
			int framesCount = scattering_I.length / pointsNumber;
			if (end > (framesCount - 1)) {
				end = framesCount - 1;
			}

			/** FRAME **/
			for (int i = start; i <= end; i++) {
				String framefileName = "frame\\frame_" + String.format("%05d", i) + ".dat";
				String fileContent = this.getH5FrameScatteringFile(i, root);
				ZipEntry zipentry = new ZipEntry(framefileName);
				zipfile.putNextEntry(zipentry);
				zipfile.write(fileContent.getBytes());
			}

			/** SUBTRACTION **/
			for (int i = start; i <= end; i++) {
				String framefileName = "subtraction\\frame_" + String.format("%05d", i) + "_sub.dat";
				String fileContent = this.getH5FrameSubtractionFile(i, root);
				ZipEntry zipentry = new ZipEntry(framefileName);
				zipfile.putNextEntry(zipentry);
				zipfile.write(fileContent.getBytes());
			}

			/** PEAKS **/
			HashMap<String, List<List<String>>> peaksData = this.getPeaks(root);
			for (String key : peaksData.keySet()) {
				List<List<String>> peakData =  peaksData.get(key);
				String framefileName = "peak\\" + key.replace(" ", "") + "_ave.dat";
				String fileContent = this.getThreeColumnStringByPeakKeys(key, peakData);
				ZipEntry zipentry = new ZipEntry(framefileName);
				zipfile.putNextEntry(zipentry);
				zipfile.write(fileContent.getBytes());
			}
			
			
			/** BUFFER AVERAGE **/
			String framefileName = "average\\buffer_" + String.format("%05d", start) + "_ave.dat";
			String fileContent = this.getH5FrameAverageBufferFile(start, root);
			ZipEntry zipentry = new ZipEntry(framefileName);
			zipfile.putNextEntry(zipentry);
			zipfile.write(fileContent.getBytes());
			zipfile.close();

		} catch (Exception e) {
			throw e;
		} finally {
			h5File.close();
		}
		return bos.toByteArray();
	}

	private HashMap<String, float[]> getQ(Group root) throws Exception {
		ArrayList<String> params = new ArrayList<String>();
		params.add("q");
		return this.getH5ParametersByExperimentId(params);
	}

	public HashMap<String, ArrayList<String>> getH5FrameScattering(int frame, boolean includeSubtraction, boolean includeBufferMerge) throws Exception {

		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
		FileFormat h5File = fileFormat.open(filepath, FileFormat.READ);
		if (h5File == null) {
			throw new Exception("Failed to open file: " + filepath);
		}
		try {
			h5File.open();
			Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File.getRootNode()).getUserObject();
			result = getH5FrameScattering(frame, includeSubtraction, includeBufferMerge, root);
		} catch (Exception e) {
			throw e;
		} finally {
			h5File.close();
		}
		return result;
	}

	private HashMap<String, List<List<String>>> getPeaks(Group root) throws Exception {
		float[] merge_I = getDataSetDataFromH5FileByName(root, "merge_I");
		float[] merge_Stdev = getDataSetDataFromH5FileByName(root, "merge_Stdev");
		float[] q = this.getQ(root).get("q");
		float[] merge_frames = getDataSetDataFromH5FileByName(root, "merge_frames");
		int pointsCount = q.length;
		int peakCount = merge_I.length / pointsCount;
		HashMap<String, List<List<String>>> result = new HashMap<String, List<List<String>>>();
		for (int i = 0; i < peakCount; i++) {
			String key = merge_frames[i * 2] + " - " + merge_frames[i * 2 + 1];
			result.put(key, new ArrayList<List<String>>());
			for (int j = 0; j < pointsCount; j++) {
				ArrayList<String> point = new ArrayList<String>();
				point.add(String.valueOf(q[j]));
				point.add(String.valueOf(merge_I[pointsCount * i + j]));
				point.add(String.valueOf(merge_Stdev[pointsCount * i + j]));
				result.get(key).add(point);
			}
		}
		return result;
	}
	
	private HashMap<String, ArrayList<String>> getH5FrameScattering(int frame, boolean includeSubtraction, boolean includeBufferMerge, Group root) throws Exception {
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		HashMap<String, float[]> resultQ = this.getQ(root);
		result.put("q", new ArrayList<String>());

		float[] scattering_I = getDataSetDataFromH5FileByName(root, "scattering_I");
		float[] scattering_Stdev = getDataSetDataFromH5FileByName(root, "scattering_Stdev");
		result.put("scattering_I", new ArrayList<String>());
		result.put("scattering_Stdev", new ArrayList<String>());

		float[] subtracted_I = null;
		float[] subtracted_Stdev = null;
		if (includeSubtraction) {
			subtracted_I = getDataSetDataFromH5FileByName(root, "subtracted_I");
			subtracted_Stdev = getDataSetDataFromH5FileByName(root, "subtracted_Stdev");
			result.put("subtracted_I", new ArrayList<String>());
			result.put("subtracted_Stdev", new ArrayList<String>());
		}

		/** Buffer Merge **/
		float[] buffer_I = null;
		float[] buffer_Stdev = null;

		if (includeBufferMerge) {
			buffer_I = getDataSetDataFromH5FileByName(root, "buffer_I");
			buffer_Stdev = getDataSetDataFromH5FileByName(root, "buffer_Stdev");
			result.put("buffer_I", new ArrayList<String>());
			result.put("buffer_Stdev", new ArrayList<String>());
		}

		/** Point depends on q **/
		int pointsNumber = resultQ.get("q").length;
		int framesCount = scattering_I.length / pointsNumber;
		if (frame > framesCount) {
			throw new Exception("frame > framesCount");
		}

		for (int i = 0; i < pointsNumber; i++) {
			result.get("q").add(String.valueOf(resultQ.get("q")[i]));

			result.get("scattering_I").add(String.valueOf(scattering_I[pointsNumber * frame + i]));
			result.get("scattering_Stdev").add(String.valueOf(scattering_Stdev[pointsNumber * frame + i]));

			if (includeSubtraction) {
				result.get("subtracted_I").add(String.valueOf(subtracted_I[pointsNumber * frame + i]));
				result.get("subtracted_Stdev").add(String.valueOf(subtracted_Stdev[pointsNumber * frame + i]));
			}

			if (includeBufferMerge) {
				result.get("buffer_I").add(String.valueOf(buffer_I[i]));
				result.get("buffer_Stdev").add(String.valueOf(buffer_Stdev[i]));
			}
		}
		return result;
	}

	/**
	 * @param experimentId
	 * @param arrayList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public HashMap<String, float[]> getH5ParametersByExperimentId(List<String> parameters) throws Exception {
		HashMap<String, float[]> result = new HashMap<String, float[]>();
		if (filepath != null) {
			FileFormat fileFormatHDF5 = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);

			// add default HDF5 modules if not present
			if (fileFormatHDF5 == null) {
				try {
					Class fileclass = Class.forName("ncsa.hdf.object.h5.H5File");
					FileFormat fileformat = (FileFormat) fileclass.newInstance();
					int i = 1;
					if (fileformat != null) {
						FileFormat.addFileFormat(FileFormat.FILE_TYPE_HDF5, fileformat);
					}
				} catch (Throwable err) {
					System.out.println(err);
					err.printStackTrace();
				}
//				fileFormatHDF5 = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
			}
			LOGGER.info(parameters);
			LOGGER.info(fileFormatHDF5);
			Enumeration fileFormatKeys = FileFormat.getFileFormatKeys();

			FileFormat h5File = fileFormatHDF5.open(filepath, FileFormat.READ);
			if (h5File == null) {
				throw new Exception("Failed to open file: " + filepath);
			}
			try {
				h5File.open();
				Group root = (Group) ((DefaultMutableTreeNode) h5File.getRootNode()).getUserObject();
				for (String parameter : parameters) {
					float[] data = getDataSetDataFromH5FileByName(root, parameter);
					for (int i = 0; i < data.length; i++) {
						if (data[i] == Float.NaN) {
							data[i] = 0;
						}
					}
					result.put(parameter, data);
				}
			} catch (Exception exp) {
				throw exp;
			} finally {
				h5File.close();
			}
			return result;
		} else {
			throw new Exception("Experiment not found ");
		}
	}

	/**
	 * Recursively print a group and its members.
	 * 
	 * @throws Exception
	 */
	private static float[] getDataSetDataFromH5FileByName(Group g, String name) throws Exception {
		java.util.List<HObject> members = g.getMemberList();
		int n = members.size();
		String indent = "    ";
		for (int i = 0; i < n; i++) {
			HObject obj = members.get(i);
			String groupName = (indent + obj);
			if (groupName.trim().equals(name)) {
				H5ScalarDS dataset = (H5ScalarDS) obj;
				return (float[]) (dataset.getData());
			}
		}
		return null;
	}

	/**
	 * the merge_I start
	 * 
	 * @param filepath
	 * @return 
	 *         {"169.0 - 270.0":[["0.03204772","0.0","0.0"],["0.03678418","0.0","0.0"
	 *         ]....]}
	 * @throws Exception
	 */
	public HashMap<String, List<List<String>>> getSampleAverages() throws Exception {
		FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
		// FileFormat h5File = fileFormat.open(filepath, FileFormat.READ);
		FileFormat h5File = fileFormat.createInstance(filepath, FileFormat.READ);
		if (h5File == null) {
			throw new Exception("Failed to open file: " + filepath);
		}
		if (!h5File.canRead()) {
			throw new Exception("Failed to read file: " + filepath);
		}
		try {

			h5File.open();
			Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File.getRootNode()).getUserObject();
			float[] merge_I = getDataSetDataFromH5FileByName(root, "merge_I");
			float[] merge_Stdev = getDataSetDataFromH5FileByName(root, "merge_Stdev");
			float[] q = this.getQ(root).get("q");
			float[] merge_frames = getDataSetDataFromH5FileByName(root, "merge_frames");
			int pointsCount = q.length;
			int peakCount = merge_I.length / pointsCount;
			HashMap<String, List<List<String>>> result = new HashMap<String, List<List<String>>>();
			for (int i = 0; i < peakCount; i++) {
				String key = merge_frames[i * 2] + " - " + merge_frames[i * 2 + 1];
				result.put(key, new ArrayList<List<String>>());
				for (int j = 0; j < pointsCount; j++) {
					ArrayList<String> point = new ArrayList<String>();
					point.add(String.valueOf(q[j]));
					point.add(String.valueOf(merge_I[pointsCount * i + j]));
					point.add(String.valueOf(merge_Stdev[pointsCount * i + j]));
					result.get(key).add(point);
				}
			}
			return result;

		} catch (Exception exp) {
			throw exp;
		} finally {
			h5File.close();
		}
	}

}
