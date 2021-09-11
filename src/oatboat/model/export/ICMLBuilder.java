package oatboat.model.export;

/**
 * Takes strings and pads them with the necessary XML-style code in preparation
 * for use in an ICML build
 * <p>
 * Input text can be preformatted by using the following tag styles:
 * 
 * <b>__double underscores__</b> will be exported <u>underlined</u>; <b>**double
 * asterisks**</b> will be exported <b>bold</b>
 * 
 * @author ballsies
 *
 */
public class ICMLBuilder {

	private String paraStyle_GeneralText = "\t\t<ParagraphStyleRange AppliedParagraphStyle=\"ParagraphStyle/general text\">\n";
	private String paraStyle_ContentsText = "\t\t<ParagraphStyleRange AppliedParagraphStyle=\"ParagraphStyle/contents paragraph\">\n";
	private String paraStyle_NutritionText = "\t\t<ParagraphStyleRange AppliedParagraphStyle=\"ParagraphStyle/nutritional paragraph\">\n";
	private String paraStyle_ExpiryText = "\t\t<ParagraphStyleRange AppliedParagraphStyle=\"ParagraphStyle/expiry date\">\n";

	private String charStyle_none = "\t\t\t<CharacterStyleRange AppliedCharacterStyle=\"CharacterStyle/$ID/[No character style]\">\n";
	private String charStyle_Semibold = "\t\t\t<CharacterStyleRange AppliedCharacterStyle=\"CharacterStyle/$ID/[No character style]\" FontStyle=\"Semibold Condensed\">\n";
	private String charStyle_Underlined = "\t\t\t<CharacterStyleRange AppliedCharacterStyle=\"CharacterStyle/underlined\">\n";
	private String charStyle_Bold = "\t\t\t<CharacterStyleRange AppliedCharacterStyle=\"CharacterStyle/$ID/[No character style]\" FontStyle=\"Condensed\">\n";

	private String charStyle_end = "\t\t\t</CharacterStyleRange>\n";
	private String paraStyle_End = "\t\t</ParagraphStyleRange>\n";

	private String contentStart = "\t\t\t\t<Content>", contentEnd = "</Content>\n";

	private String paraBreak = "\t\t\t\t<Br />\n";

	public String makeHeadingBlock(String heading) {
		return paraStyle_GeneralText + charStyle_Semibold + content(heading) + paraBreak + charStyle_end
				+ paraStyle_End;
	}

	public String makeSubheadingBlock(String subhead) {
		return paraStyle_GeneralText + charStyle_none + content(subhead) + paraBreak + charStyle_end + paraStyle_End;
	}

	public String makeContentsBlock(String contents) {
		return paraStyle_ContentsText + charStyle_none + format(contents) + paraBreak + charStyle_end + paraStyle_End;
	}

	public String makeNutritionBlock(String nutrition) {
		return paraStyle_NutritionText + charStyle_none + format(nutrition) + paraBreak + charStyle_end + paraStyle_End;
	}

	public String makeFooterBlock(String footer) {
		return paraStyle_GeneralText + charStyle_none + format(footer) + paraBreak + charStyle_end + paraStyle_End;
	}

	public String makeExpiryDate(String expiry) {
		return paraStyle_ExpiryText + charStyle_none + content(expiry) + charStyle_end + paraStyle_End;
	}

	// returns a given string padded in <Content> tags
	private String content(String text) {
		return contentStart + text + contentEnd;
	}

	/**
	 * searches through given text for custom formatting tags (double underscores or
	 * double asterisks), and returns the text as content padded in relevant
	 * character style blocks
	 */
	private String format(String text) {
		StringBuilder formatted = new StringBuilder();
		formatted.append(contentStart);
		int i = 0;
		while (i < text.length()) {

			// pad in underlined character style block
			if (i < text.length() - 4 && text.substring(i, i + 2).equals("__")) {
				int end = i + getNext("__", text.substring(i + 2, text.length()));
				formatted
						.append((contentEnd + charStyle_end + charStyle_Underlined + content(text.substring(i + 2, end))
								+ charStyle_end + charStyle_none + contentStart).toString());
				i = end + 2;
			}

			// pad in bold character style block
			else if (i < text.length() - 4 && text.substring(i, i + 2).equals("**")) {
				int end = i + getNext("**", text.substring(i + 2, text.length()));
				formatted.append((contentEnd + charStyle_end + charStyle_Bold + content(text.substring(i + 2, end))
						+ charStyle_end + charStyle_none + contentStart).toString());
				i = end + 2;
			}

			// assume there is no formatting for this character, add it to builder
			else {
				formatted.append(text.charAt(i));
				i++;
			}
		}
		formatted.append(contentEnd);
		return formatted.toString();
	}

	private int getNext(String query, String input) {
		for (int i = 0; i < input.length() - 1; i++) {
			if (input.substring(i, i + 2).equals(query)) {
				return i + 2;
			}
		}
		return input.length();
	}
}
