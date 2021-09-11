package oatboat.model.export;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 * Class to export the full formatted string of contents and nutritional
 * information to an Adobe InCopy ICML file, which can be imported/placed in an
 * InDesign project.
 * 
 * @author ballsies
 *
 */
public class ExportToICML {
	
	public static final String icmlFileName = "snippet.icml";

	/**
	 * Takes an array of Strings and exports a .ICML InCopy file, which can then be
	 * placed in an InDesign project.
	 * 
	 * 
	 * @param filename    the complete filename for the produced .icml file
	 * @param storyBlocks any number of Strings
	 */
	public static boolean makeFile(String filename, String... storyBlocks) throws Exception {
		// the main stringbuilder begins with static file header information
		StringBuilder finalText = new StringBuilder(ICMLConstant.mainFileHeader);

		/**
		 * the working body of a ICML file is the DOCUMENT block, which contains the
		 * rest of the data required; minimal requirements for the resulting file to
		 * operate are the definitions of the character styles and paragraph styles; and
		 * the STORY block which holds the copy.
		 */
		StringBuilder documentBlock = new StringBuilder(ICMLConstant.documentBlockHeader);
		documentBlock.append(ICMLConstant.rootStyleGroups);

		/**
		 * next get the string[] and put each part in relevant paragraphs while also
		 * checking for internal formatting styles. Each change in word style (e.g.
		 * underlining) requires a new block of XML style code to be produced for the
		 * stringbuilder; while each paragraph also requires a similar new XML-style
		 * code block.
		 * <p>
		 * Each paragraph is represented for clarity by a descriptive and functional
		 * paragraph style name.
		 */

		// start the STORY block, which holds the blocks defining the copy and their
		// formats
		documentBlock.append(ICMLConstant.storyHeader);

		// put code blocks from given arguments
		for (String storyBLock : storyBlocks) {
			documentBlock.append(storyBLock);
		}

		// lastly put everything together and finalise the story and document blocks
		finalText.append(documentBlock);
		finalText.append(ICMLConstant.storyAndDocumentFooter);

		// finally, attempt to write the file (needs to be UTF8 for cross platform reliability 
		BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename), StandardCharsets.UTF_8);
		writer.write(finalText.toString());
		writer.close();
		return true;
	}

	public static void main(String[] args) {
		ICMLBuilder build = new ICMLBuilder();
		String[] storyBlockContents = { build.makeHeadingBlock("OVESNÉ VLOČKY PŘES NOC"),
				build.makeSubheadingBlock("Bez přidaného cukru. Bez konzervantů."),
				build.makeContentsBlock("Contents!"), 
				build.makeFooterBlock("Skladujte při teplotě od 2°C do 8°C **Hmotnost: 300g**. Spotřebujte do:"),
				build.makeExpiryDate(LocalDate.parse(LocalDate.now().toString()).plusDays(3).toString()) 
				};

		try {
			ExportToICML.makeFile(icmlFileName, storyBlockContents);

		} catch (Exception e) {
			System.out.println("didn't succeed exporting the icml file");
		}

	}

}
