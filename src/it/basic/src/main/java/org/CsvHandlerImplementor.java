//package org;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.charset.Charset;
//
//import com.github.blutorange.multiproperties_maven_plugin.handler.HandlerImplementor;
//import com.github.blutorange.multiproperties_maven_plugin.handler.HandlerImplementorContext;
//import com.github.blutorange.multiproperties_maven_plugin.parser.ItemMatcher;
//import com.github.blutorange.multiproperties_maven_plugin.parser.Property;
//
//public class CsvHandlerImplementor implements HandlerImplementor<CsvHandler> {
//	@Override
//	public String getName() {
//		return "My Custom Handler";
//	}
//
//	@Override
//	public void handleProperties(HandlerImplementorContext<CsvHandler> ctx) throws Exception {
//		final var config = ctx.getConfiguration();
//		final var key = ctx.getColumnKey();
//		final var outputFile = ctx.getInputFile().toString() + "_" + key + ".csv";
//		try (final var writer = new FileWriter(outputFile, Charset.forName(config.getEncoding()))) {
//			for (final var item : ctx.getItems()) {
//				item.match(new ItemMatcher<Void, IOException>() {
//					@Override
//					public Void property(Property property) throws IOException {
//						final var value = property.getResolvedValue(ctx.getColumnKey());
//						writer.write('"');
//						writer.write(escapeCsv(key));
//						writer.write('"');
//						writer.write(config.getSeparator());
//						writer.write('"');
//						writer.write(escapeCsv(value));
//						writer.write('"');
//						writer.write('\n');
//						return null;
//					}
//				});
//			}
//		}
//	}
//
//	@Override
//	public CsvHandler parseConfig(String value) {
//		final var handler = new CsvHandler();
//		final var parts = value.split("\\|");
//		if (parts.length >= 1) {
//			handler.setEncoding(parts[0]);
//		}
//		if (parts.length >= 2) {
//			handler.setSeparator(parts[1]);
//		}
//		return handler;
//	}
//
//	private static String escapeCsv(String value) {
//		final var sb = new StringBuilder();
//		final var it = value.codePoints().iterator();
//		while (it.hasNext()) {
//			final var c = it.nextInt();
//			switch (c) {
//			case '"':
//				sb.append("\"\"");
//				break;
//			default:
//				sb.appendCodePoint(c);
//				break;
//			}
//		}
//		return sb.toString();
//	}
//}
