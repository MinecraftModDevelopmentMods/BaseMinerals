package com.mcmoddev.baseminerals.init;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterialType.MaterialType;
import com.mcmoddev.lib.util.Config.Options;

/**
 * This class initializes all of the materials in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Materials extends com.mcmoddev.lib.init.Materials {

	protected Materials() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {

		final List<String> materials = Arrays.asList(MaterialNames.LITHIUM, MaterialNames.NITER,
				MaterialNames.PHOSPHORUS, MaterialNames.POTASH, MaterialNames.SALT, MaterialNames.SALTPETER,
				MaterialNames.SULFUR);

		materials.stream().filter(Options::isMaterialEnabled).forEach(name -> createMaterial(name, getType(name),
				getHardness(name), getStrength(name), getMagic(name), getColor(name)));

		if (Options.isMaterialEnabled(MaterialNames.SILICON)) {
			final String name = MaterialNames.SILICON;
			createAlloyMaterial(name, getType(name), getHardness(name), getStrength(name), getMagic(name),
					getColor(name));
		}
	}

	private static MaterialType getType(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.LITHIUM:
			case MaterialNames.SILICON:
				return MaterialType.METAL;
			case MaterialNames.NITER:
			case MaterialNames.PHOSPHORUS:
			case MaterialNames.POTASH:
			case MaterialNames.SALT:
			case MaterialNames.SALTPETER:
			case MaterialNames.SULFUR:
				return MaterialType.MINERAL;
			default:
				return MaterialType.MINERAL;
		}
	}

	private static int getColor(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.LITHIUM:
				return 0xFFC5C8C1;
			case MaterialNames.NITER:
				return 0xFF000000;
			case MaterialNames.PHOSPHORUS:
				return 0xFF000000;
			case MaterialNames.POTASH:
				return 0xFF000000;
			case MaterialNames.SALT:
				return 0xFF000000;
			case MaterialNames.SALTPETER:
				return 0xFF000000;
			case MaterialNames.SILICON:
				return 0xFFC5C8C1;
			case MaterialNames.SULFUR:
				return 0xFF000000;
			default:
				return 0xFF000000;
		}
	}

	private static double getMagic(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.LITHIUM:
			case MaterialNames.SILICON:
				return 4.5d;
			case MaterialNames.NITER:
			case MaterialNames.PHOSPHORUS:
			case MaterialNames.POTASH:
			case MaterialNames.SALT:
			case MaterialNames.SALTPETER:
			case MaterialNames.SULFUR:
				return 0d;
			default:
				return 1.0d;
		}
	}

	private static double getStrength(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.LITHIUM:
			case MaterialNames.SILICON:
				return 8d;
			case MaterialNames.NITER:
			case MaterialNames.PHOSPHORUS:
			case MaterialNames.POTASH:
			case MaterialNames.SALT:
			case MaterialNames.SALTPETER:
			case MaterialNames.SULFUR:
				return 2d;
			default:
				return 1.0d;
		}
	}

	private static double getHardness(@Nonnull final String name) {
		switch (name) {
			case MaterialNames.LITHIUM:
			case MaterialNames.SILICON:
				return 8d;
			case MaterialNames.NITER:
			case MaterialNames.PHOSPHORUS:
			case MaterialNames.POTASH:
			case MaterialNames.SALT:
			case MaterialNames.SALTPETER:
			case MaterialNames.SULFUR:
				return 5d;
			default:
				return 1.0d;
		}
	}
}
